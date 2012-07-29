package com.rawabi.xtml;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.apache.log4j.Logger;

public class XtmlInterface {
	
	protected static final int SUCCESS = 0 ;
	protected static final int DB_ERROR = -99 ;
	protected static final int PIN_NOT_FOUND = -1 ;
	protected static final int PIN_ALREADY_ACTIVE = -2 ;
	protected static final int ANI_ALREADY_ASSIGNED = -3 ;
	protected static final int INVALID_OFFERING = -4 ;
	
    private static Logger logger = Logger.getLogger(XtmlInterface.class);
	
    static protected ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
    
    
    static public int Init() {
        
        Session session = null ;
        SessionFactory sessionFactory = null ;
        int rc = DB_ERROR ;

        try {
                
                Properties prop = System.getProperties();
                System.out.println("Classpath is: " + prop.getProperty("java.class.path")) ;
                
                logger.info("--------------   XTMLApi.Init   --  Starting  ------------------");

                 ;
                sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");       
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
			long serviceProviderId
			) {
		
		int rc = SUCCESS ;
		
		SessionFactory sessionFactory = null ;
		Session session = null ;
		
        sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");       
        session = sessionFactory.openSession() ;
		
		
		
		return rc ;
	}

}
