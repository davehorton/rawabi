package com.rawabi.xtml;

import java.math.BigDecimal;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.log4j.Logger;

import com.rawabi.constants.PactolusConstants;
import com.rawabi.model.*;

public class XtmlInterface {
	
	protected static final int SUCCESS = 0 ;
	protected static final int DB_ERROR = -99 ;
	protected static final int PIN_NOT_FOUND = -1 ;
	protected static final int PIN_ALREADY_ACTIVE = -2 ;
	protected static final int ANI_ALREADY_ASSIGNED = -3 ;
	protected static final int INVALID_OFFERING = -4 ;
	protected static final int INVALID_SP_ID = -5 ;
	protected static final int PIN_SP_NOMATCH = -6 ;
	protected static final int LOT_INVALID_STATE = -7 ;
	
    private static Logger logger = Logger.getLogger(XtmlInterface.class);
	
    static protected ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
    static protected SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");       
   
    
    static public int Init() {
        
        Session session = null ;
        int rc = DB_ERROR ;

        try {
                
                Properties prop = System.getProperties();
                System.out.println("Classpath is: " + prop.getProperty("java.class.path")) ;
                
                logger.info("--------------   XTMLApi.Init   --  Starting  ------------------");

                 ;
                session = sessionFactory.openSession() ;
                rc = SUCCESS ;


                logger.info("--------------   XTMLApi.Init   --  Ending  ------------------");
                
        } catch (Exception ex) {
                logger.error("Caught exception in XtmlApi.Init", ex);
        } finally {
                if( null != session ) session.close() ;
        }                       

        return rc ;
    }

	
	static public int RegisterPrepaidAuthAni(
			String xtmlSessionId,
			String pin,
			String ani,
			long serviceProviderId,
			long numExpirationDays, 
			StringBuffer newPin
			) {
		
		int rc = DB_ERROR ;
		
		Session session = null ;
		Transaction transaction = null ;
		
		newPin.setLength(0) ;
		
		try {
			
			logger.info("RegisterPrepaidAuthAni----starting------") ;
			logger.info("xtmlSessionId:     " + xtmlSessionId) ;
			logger.info("pin:               " + pin ) ;
			logger.info("ani:               " + ani ) ;
			logger.info("serviceProviderId: " + serviceProviderId) ;
			logger.info("numExpirationDays: " + numExpirationDays) ;
		
			if( numExpirationDays < 30 ) numExpirationDays = 30 ;
			
			session = sessionFactory.openSession() ;	
			
			ServiceProvider sp = (ServiceProvider) session.load(ServiceProvider.class, BigDecimal.valueOf(serviceProviderId) ) ;
			if( null == sp ) {
				logger.error("Invalid service provider id: " + serviceProviderId ) ;
				return INVALID_SP_ID ;
			}
			
			Boolean bNewSubscriber = true ;
			BigDecimal subServiceProviderId ;
			Subscriber sub = null ;
			PreActivatedSubscribers pre = (PreActivatedSubscribers)  session.createCriteria(PreActivatedSubscribers.class)
					.add(Restrictions.eq("pin", pin))
					.uniqueResult() ;
			if( null == pre ) {
				
				sub = (Subscriber) session.createCriteria(Subscriber.class)
						.add(Restrictions.eq("pin", pin))
						.uniqueResult() ;
				if( null != sub ) {
					bNewSubscriber = false ;
					subServiceProviderId = sub.getServiceProviderId() ;
					
					/* we can make an existing subscriber an auth ani subscriber, but only if the subscriber doesn't already have an auth ani */
					if( !sub.getSubAuthAnis().isEmpty() ) {
						logger.error("This pin has already been used: " + pin) ;
						return PIN_ALREADY_ACTIVE ;
					}
					logger.info("Pin provided has already been activated, but we will make the existing subscriber an auto-ani subscriber now: " + pin ) ;					
				}
				else {
					/* since we mangle the pin after registering it, check for the mangled version of this pin */
					sub = (Subscriber) session.createCriteria(Subscriber.class)
							.add(Restrictions.eq("pin", manglePin( pin ) ) )
							.uniqueResult() ;
					if( null != sub ) {
						logger.error("Pin provided has already been activated for a different ANI") ;
						return PIN_ALREADY_ACTIVE ;
					}
					logger.error("Invalid/Unknown pin: " + pin) ;
					return PIN_NOT_FOUND ;
				}
			}
			else {
				subServiceProviderId = pre.getServiceProvider().getServiceProviderId() ;
			}
			
			if( 0 != subServiceProviderId.compareTo( sp.getServiceProviderId() ) ) {
				logger.error("Pin belongs to different service provider than access number; pin belongs to " + pre.getServiceProvider().getName() + 
						" while access number belongs to service provider " + sp.getName() ) ;
				return PIN_SP_NOMATCH ;
			}
			
			/* make sure the phone number isn't already in the database for someone else */
			SubAuthAni authAni = (SubAuthAni) session.createCriteria(SubAuthAni.class)
					.add(Restrictions.eq("phoneNumber", ani))
					.createCriteria("serviceProvider")
						.add(Restrictions.eq("serviceProviderId", sp.getServiceProviderId()))
					.uniqueResult() ;
			if( null != authAni ) {
				logger.error("phone number " + ani + " is already in use for subscriber with pin: " + authAni.getSubscriber().getPin() ) ;
				return ANI_ALREADY_ASSIGNED ;				
			}

			if( bNewSubscriber ) {
				
				/* retrieve the lot */
				Lot lot = pre.getLot() ; 
				if( null == lot ) {
					logger.error("lot not found for preactivated subscriber with pin: " + pre.getPin() ) ;
					return DB_ERROR ;
				}
				
				/* verify the lot is in a proper state to vend us a pin for use */
				Integer lotStatus = lot.getLotStatus().intValue() ;
				if( PactolusConstants.LOT_PROCESSED != lotStatus && PactolusConstants.LOT_ACTIVATED != lotStatus ) {
					logger.error("lot status is not valid for pin activation: " + lotStatus ) ;
					return LOT_INVALID_STATE ;
				}
				
				/* if there is an activation group for this pin, then retrieve it */
				EvtPrepaidActivation activationGroup = null ;
				if( null != pre.getActivationId() ) {
					activationGroup = (EvtPrepaidActivation) session.load(EvtPrepaidActivation.class, pre.getActivationId() ) ;
				}
						
				ProductOffering offering = lot.getProductOffering() ;
				
				Query q = session.createSQLQuery("SELECT osx.service_id " +
						"FROM offering_service_xref osx, svc_prepaid_calling spc " +
						"WHERE osx.offering_id = ? " +
						"AND osx.service_id = spc.service_id ") ;
				q.setBigDecimal(0, offering.getOfferingId() ) ;
				BigDecimal svcId = (BigDecimal) q.uniqueResult() ;
	
				transaction = session.beginTransaction() ;
				
				/* now create an entry in the subscriber table */
				sub = new Subscriber() ;
				sub.setSubscriberId( pre.getSubscriberId().longValue() ) ;
				sub.setCurrPrepaidBalance( lot.getInitialBalance() ) ;
				sub.setInitialBalance( lot.getInitialBalance() ) ;
				sub.setFirstCallDate(null) ;
				sub.setFirstUseDate(null) ;
				sub.setLotId(lot.getLotId() ) ;
				
				/* special requirement from Milan here: after registering the ANI, we do not want the pin to ever be able to be used again for pin dialing.
				 * so we mangle it by adding a character.
				 */
				sub.setPin( manglePin( pre.getPin() ) ) ;
				
				sub.setPinPassword( pre.getPinPassword() ) ;
				sub.setServiceProviderId( sp.getServiceProviderId() ) ;
				sub.setCurrencyId( offering.getCurrencyId() ) ;
				sub.setCallingSvcId( svcId ) ;
				sub.setActivationDate( pre.getActivationDate() ) ;
				sub.setDisabledFlag('F') ;
				sub.setBillingType( BigDecimal.valueOf(1) ) ;	//1=prepaid,2=postpaid,3=broadband
				sub.setLotControlNumber( pre.getLotControlNumber() );
				sub.setLotSeqNumber(pre.getLotSeqNumber()) ;
				
				/* special requirement from Milan here: regardless of what type expiration the lot has, the account will now have days from last use */
				//sub.setExpirationType(BigDecimal.valueOf( (long) PactolusConstants.LAST_USE_EXPIRATION) );
				//sub.setNumExpirationDays(BigDecimal.valueOf(numExpirationDays)) ;
				
				/* ok, change request: set the expiration type and days from activation group if it exists, or lot if not */
				if( null != activationGroup ) {
					sub.setExpirationType( activationGroup.getExpirationType() ) ;
					sub.setNumExpirationDays( activationGroup.getNumExpirationDays() ) ;
				}
				else {
					sub.setExpirationType( lot.getExpirationType() ) ;
					sub.setNumExpirationDays( lot.getNumExpirationDays() ) ;
				}
								
				sub.setLanguageId( lot.getProductOffering().getLanguageId() ) ;
				sub.setConfOperatorAssistType(BigDecimal.valueOf(0)); 
				sub.setDirectCallFlag('F') ;
				sub.setOffplanAlertPlayed(BigDecimal.ZERO) ;
				sub.setFirstBillcyclePlayed('F') ;
				sub.setOnplanAlertPlayed(BigDecimal.ZERO) ;
				sub.setBilledSequence(BigDecimal.ZERO) ;
				sub.setConfOperatorAssistType(BigDecimal.ZERO) ;
				sub.setBucketRefillWarningFlag('F') ;
				sub.setBucketExhaustWarningFlag('F') ;
				sub.setAutoPayFlag('F') ;
				sub.setReceiveBillingEmailFlag('F') ;
				sub.setOverrideDeptE911Address('F') ;
				sub.setAgreedTo911TermsFlag('F') ;
				sub.setFirstUseFeeState(BigDecimal.ZERO) ;
							
				session.save( sub ) ;
	
				/* tie subscriber to offering */
				SubOfferingXref sxo = new SubOfferingXref() ;
				sxo.setId( new SubOfferingXrefId( BigDecimal.valueOf( sub.getSubscriberId() ), offering.getOfferingId() ) ) ;
				sxo.setPrimaryFlag('T') ;
				session.save( sxo ) ;
				
				session.delete( pre ) ;		
			}
			else {
				/* special requirement from Milan here: after registering the ANI, we do not want the pin to ever be able to be used again for pin dialing.
				 * so we mangle it by adding a character.
				 */

				sub.setPin( manglePin( pre.getPin() ) ) ;
				
				
				
				/* special requirement from Milan here: regardless of what type expiration the lot has, the account will now have days from last use 
				 * note: this is different from how we treat new pins, I know.
				 * */
				sub.setExpirationType(BigDecimal.valueOf( (long) PactolusConstants.LAST_USE_EXPIRATION) );
				sub.setNumExpirationDays(BigDecimal.valueOf(numExpirationDays)) ;
				session.save( sub ) ;
			}
			
			/* add the authorized ani */
			
			authAni = new SubAuthAni() ;
			authAni.setPhoneNumber(ani) ;
			authAni.setSubscriber(sub) ;
			authAni.setServiceProvider( sp ) ;
			authAni.setStatus("0") ;
			
			session.save( authAni ) ;	
									
			transaction.commit() ;
			
			newPin.append( sub.getPin() ) ;
			
			logger.info("pin was successfully activated, pin number has been changed to: " + newPin.toString() 
					) ;
			
			rc = SUCCESS ;
		
		} catch( HibernateException e ) {
			if( null != transaction ) transaction.rollback() ;
			logger.error("Hibernate error", e) ;
		}
		finally {
			if( null != session ) session.close() ;
		}
		
		return rc ;
	}
	
	static protected String manglePin( String pin ) {
		return pin + "x" ;
	}

}
