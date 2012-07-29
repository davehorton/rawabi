// <revision >$Id: dialing_plan_utils.jsh,v 1.24.2.1 2009/08/27 10:30:42 egaudette Exp $</revision>
// <label >$Name: PCS-A-4-1-1-1-4-c6 $</label>
function js_translate_destination(customer_code, destination, home_country_code, vpn_prefix, vpn_dest_length)
{
	Server.logInfo("IN TRANSLATE DESTINATION.");
	Server.logInfo("INPUT CUSTOMER CODE IS: " + customer_code);
	Server.logInfo("INPUT DESTINATION IS: " + destination);
	Server.logInfo("INPUT DEFAULT COUNTRY CODE IS: " + home_country_code);
	Server.logInfo("INPUT VPN PREFIX IS: " + vpn_prefix);
	Server.logInfo("INPUT VPN DEST LENGTH IS: " + vpn_dest_length);

	var inputString = new String(destination);
	var translatedString = new String(destination);
	//hard code international prefix +
	var intl_prefix = new String("+");

	/********************** TSYSTEM ********************************/	
	if ("tsystems" == customer_code)
	{
		//translate destination for TSystem
		Server.logInfo("TSYSTEM CUSTOMER, NEED TO TRANSLATE DESTINATION");
			
		if ( ('+' == inputString.charAt(0)))
		{
			Server.logInfo("ALREADY IN E.164 FORMAT DEST, DO NOTHING.");
		}
		else if (vpn_dest_length == inputString.length)
		{
			Server.logInfo("PRIVATE VPN NUMBER, PREPEND VPN PREFIX " + vpn_prefix);
			translatedString = vpn_prefix + translatedString;
		}
		else if (1 == home_country_code && 0 == inputString.indexOf("011"))
		{
			//assume destination already has country code
			Server.logInfo("COUNTRY CODE IS U.S, STARTS WITH 011, strip 011 and replace with international prefix " + intl_prefix);
			translatedString = translatedString.slice(3);
			translatedString = intl_prefix + translatedString;
		}
		else if (home_country_code != null && 0 == inputString.indexOf("00"))
		{
			//assume destination already has country code
			Server.logInfo("INTERNATIONAL NUMBER , STARTS WITH 00, strip 00 and replace with international prefix " + intl_prefix);
			translatedString = translatedString.slice(2);
			translatedString = intl_prefix + translatedString;
		}
		else if (home_country_code != null && 0 != inputString.indexOf("00"))
		{
			Server.logInfo("NATIONAL NUMBER, APPLY DEFAULT COUNTRY CODE");
			if ( (33 != home_country_code) && ('0' == inputString.charAt(0)) )
			{
				//strip leading zero before applying country code EXCEPT Italy number
				Server.logInfo("NON ITALY NUMBER, STRIP LEADING ZERO");
				translatedString = translatedString.slice(1);
			}
			Server.logInfo("PREPEND INTL PREFIX AND DEFAULT COUNTRY CODE");
			translatedString = intl_prefix + home_country_code + translatedString;
		}
		else
		{
			Server.logInfo("DESTINATION DOES NOT MATCH ANY CONDITION, DO NOTHING.");
		}
	} //end if tsystem
	
	/********************** SLOVAKIA ********************************/
	else if ("slovakia" == customer_code)
	{
		//translate destination for Slovakia
		Server.logInfo("SLOVAKIA CUSTOMER, NEED TO TRANSLATE DESTINATION");

		if ( 0 == inputString.indexOf("00") )
		{
			Server.logInfo("DEST BEGIN WITH 00, STRIP 00 AND REPLACE WITH international prefix " + intl_prefix);
			translatedString = translatedString.slice(2);
			translatedString = intl_prefix + translatedString;
		}
		else if ( '0' == inputString.charAt(0) )
		{
			Server.logInfo("DEST BEGIN WITH 0, STRIP 0 AND REPLACE WITH  international prefix and default country code " + intl_prefix + ", " + home_country_code);
			translatedString = translatedString.slice(1);
			translatedString = intl_prefix + home_country_code + translatedString;
		}
		else
		{
			Server.logInfo("NO MATCH, DO NOTHING WITH DESTINATION");
		}
	} //end if slovakia
	else if("sawtel" == customer_code){
            //translate destination for sawtel
            Server.logInfo("CUSTOMER IS SAWTEL OR E164 DIALING ENABLED");
            if ( 0 == inputString.indexOf("011") )
            {
                    Server.logInfo("DEST BEGIN WITH 011, STRIP 011, ADD + ");
                    translatedString = "+" + translatedString.slice(3);
                    Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
            }
            else if ( 0 == inputString.indexOf("0") )
            {
                    Server.logInfo("DEST BEGIN WITH 0, STRIP ZERO, ADD + ");
                    translatedString = "+" + translatedString.slice(1);
                    Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
            }
            else if ( '1' == inputString.charAt(0) )
            {
                    Server.logInfo("DEST BEGIN WITH 1, ADD + ");
                    translatedString = "+" + translatedString;
                    Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
            }
            else if ( 0 == inputString.indexOf("+1") )
            {
                    Server.logInfo("DEST BEGIN WITH +1, E.164 ALREADY DO NOTHING ");
                    Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
            }
            else
            {
                    Server.logInfo("STRIP NOTHING, ADD +1 ");
                    translatedString = "+1" + translatedString;
                    Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
            }
        }
	else
	{
		Server.logInfo("DOES NOT MATCH ANY CUSTOMER, DO NOTHING.");
	}
	Server.logInfo("TRANSLATED DESTINATION IS: " + translatedString);
	return translatedString;
}

function js_translate_destination_broadband(customer_code, destination, home_country_code, on_net, ani)
{
	var inputString = new String(destination);
	var translatedString = new String(destination);
	if( "pactolus" == customer_code ) {

		if( !on_net ) {
			
			Server.logInfo("js_translate_destination_broadband: translating off-net destination: " + destination);
			//put international numbers in E.164 format
			//if( 0 == inputString.indexOf("011") ) {
			//	translatedString = "+" + inputString.slice(3) ;
			//}

			//dial domestic numbers as 1+number
			if( 10 == inputString.length ) {
				translatedString = "1" + inputString ;
			}
			// Otherwise, don't do anything. Pass the number as it is.
 		}
		else {
			Server.logInfo("js_translate_destination_broadband: translating on-net destination: " + destination);
		}	 
	}
	if( "usadatanet" == customer_code ) {

		if( !on_net ) {
			
			//Server.logInfo("js_translate_destination_broadband: translating off-net destination: " + destination);
			//Server.logInfo("js_translate_destination_broadband: ANI: " + ani);

			//special rules for domestic numbers
			if( 0 != inputString.indexOf("011") && 0 != inputString.indexOf("*") ) {
			
				//allow 7-digit dialing (no area code)
				if( 7 == inputString.length && 10 == ani.length ) {
					translatedString = ani.slice(0,3) + inputString ;
				}
				//allow 4-digit dialing (no area code or exchange)
				else if( 4 == inputString.length && 10 == ani.length ) {
					translatedString = ani.slice(0,6) + inputString ;
				}
			}
		}
		else {
			//Server.logInfo("js_translate_destination_broadband: translating on-net destination: " + destination);
		}	 
	}
	if( "sawtel" == customer_code ) {
		if( !on_net ) {
			//Server.logInfo("Received an off-net call: " + inputString ) ;
			if( 0 == inputString.indexOf("1") && 11 == inputString.length ) {
				//Server.logInfo("Adding + to 1<areacode> call") ;
				translatedString = "+" + translatedString ;
			}
			if( 10 == inputString.length ) {
				//Server.logInfo("Received domestic call, prepend +1" ) ;
				translatedString = "+1" + inputString ;
			}
			if( 0 == inputString.indexOf("011") ) {
				//Server.logInfo("Received intl call, prepend +" ) ;
				translatedString = "+" + inputString.substring(3) ;
			}
		}
	}

	if ("latinode" == customer_code)
	{
		if ( !on_net ) {

			//translate destination for latinode
			Server.logInfo("CUSTOMER IS LATINODE,CHECK LOCAL DIALING RULES");
	
			if ( home_country_code == 9)//origination country is Argentina
			{
				Server.logInfo("ORIG COUNTRY IS ARGENTINA");

				if ( 0 == destination.indexOf("0") )
				{
					Server.logInfo("DEST BEGIN WITH 0, DO NOTHING");
				}
				if ( null == ani || ani.length < 4)
				{
					Server.logInfo("ANI LESS THAN 4 DIGITS, DO NOTHING");
				}
				else if ( 0 == destination.indexOf("15") && (destination.length >= 8 && destination.length <=10))
				{	
					Server.logInfo("DEST BEGIN WITH 15 AND 8-10 DIGITS, TACK ON DIGITS FROM ANI");
					translatedString = ani.substring(0, 12-destination.length) + translatedString ;	
				}else if ( destination.length >= 6 && destination.length <=8)
				{
					Server.logInfo("DEST is 6-8 DIGITS, TACK ON DIGITS FROM ANI");
					translatedString = ani.substring(0, 10-destination.length) + translatedString ;
				}else
				{
					Server.logInfo("DEST DOES NOT MATCH ANY CONDITION, LEAVE IT ALONE");	
			}
			
			}else{
				Server.logInfo("ORIG COUNTRY IS NOT ARGENTINA, NO CHANGES TO DEST");
			}
		}
	}	 	 
	return translatedString ;
}

function js_translate_calling_number_broadband(customer_code, callingNumb )
{
	var inputString = new String(callingNumb);
	var translatedString = new String(callingNumb);

	if( "sawtel" == customer_code ) {
		if( 0 == inputString.indexOf("1") && 11 == inputString.length ) {
			//Server.logInfo("Adding "+" to 1<areacode> of calling number") ;
			translatedString = "+" + translatedString ;
		}
		else if( 10 == inputString.length ) {
			//Server.logInfo("Prepend "+1" to domestic calling number" ) ;
			translatedString = "+1" + inputString ;
		}
	}	 
	return translatedString ;
}


function js_translate_destination(customer_code, destination, ani, origCountryID)
{
	Server.logInfo("IN TRANSLATE DESTINATION.");
	Server.logInfo("INPUT CUSTOMER CODE IS: " + customer_code);
	Server.logInfo("INPUT DESTINATION IS: " + destination);
	Server.logInfo("INPUT ANI IS: " + ani);

	var inputString = new String(destination);
	var translatedString = new String(destination);

	/********************** TSYSTEM ********************************/	
	if ("latinode" == customer_code)
	{
		//translate destination for latinode
		Server.logInfo("CUSTOMER IS LATINODE,CHECK LOCAL DIALING RULES");
			
		if ( origCountryID == 9)//origination country is Argentina
		{
			Server.logInfo("ORIG COUNTRY IS ARGENTINA");

			if ( 0 == inputString.indexOf("0") )
			{
				Server.logInfo("DEST BEGIN WITH 0, DO NOTHING");
			}
			if ( null == ani || ani.length < 4)
			{
				Server.logInfo("ANI LESS THAN 4 DIGITS, DO NOTHING");
			}
			else if ( 0 == inputString.indexOf("15") && (inputString.length >= 8 && inputString.length <=10))
			{
				Server.logInfo("DEST BEGIN WITH 15 AND 8-10 DIGITS, TACK ON DIGITS FROM ANI");
				translatedString = ani.substring(0, 12-inputString.length) + translatedString ;
			}else if ( inputString.length >= 6 && inputString.length <=8)
			{
				Server.logInfo("DEST is 6-8 DIGITS, TACK ON DIGITS FROM ANI");
				translatedString = ani.substring(0, 10-inputString.length) + translatedString ;
			}else
			{
				Server.logInfo("DEST DOES NOT MATCH ANY CONDITION, LEAVE IT ALONE");
		}
			
		}else{
			Server.logInfo("ORIG COUNTRY IS NOT ARGENTINA, NO CHANGES TO DEST");
		}
	}
	else if( "teletek" == customer_code ) {

		/* teletek is a vercom customer in Turkey, for local dialing
		   people may dial '0x' (x=[2-5]), but we need to present
 		   it to the switch as '90x'
		*/
		if( 0 == inputString.indexOf("02") ||
			0 == inputString.indexOf("03") ||
			0 == inputString.indexOf("04") ||
			0 == inputString.indexOf("05") ) {

			translatedString = "9" + translatedString ;
			Server.logInfo("Translated Destination is: " + translatedString);
		}
	}
        else if("sawtel" == customer_code){
            //translate destination for sawtel
            Server.logInfo("CUSTOMER IS SAWTEL OR E164 DIALING ENABLED");
            if ( 0 == inputString.indexOf("011") )
            {
                    Server.logInfo("DEST BEGIN WITH 011, STRIP 011, ADD + ");
                    translatedString = "+" + translatedString.slice(3);
                    Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
            }
            else if ( 0 == inputString.indexOf("0") )
            {
                    Server.logInfo("DEST BEGIN WITH 0, STRIP ZERO, ADD + ");
                    translatedString = "+" + translatedString.slice(1);
                    Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
            }
            else if ( '1' == inputString.charAt(0) )
            {
                    Server.logInfo("DEST BEGIN WITH 1, ADD + ");
                    translatedString = "+" + translatedString;
                    Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
            }
            else if ( 0 == inputString.indexOf("+1") )
            {
                    Server.logInfo("DEST BEGIN WITH +1, E.164 ALREADY DO NOTHING ");
                    Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
            }
            else
            {
                    Server.logInfo("STRIP NOTHING, ADD +1 ");
                    translatedString = "+1" + translatedString;
                    Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
            }
        }
	return translatedString ;
	
}
function js_translate_destination_post_rating(customer_code, destination, ani, origCountryID, e164DialingFlag)
{
	Server.logInfo("IN TRANSLATE DESTINATION.");
	Server.logInfo("INPUT CUSTOMER CODE IS: " + customer_code);
	Server.logInfo("INPUT DESTINATION IS: " + destination);
	Server.logInfo("INPUT ANI IS: " + ani);
        Server.logInfo("E164 DIALING IS: " + e164DialingFlag);

	var inputString = new String(destination);
	var translatedString = new String(destination);

	/********************** SWAWTEL *******************************/	
	if ("sawtel" == customer_code || e164DialingFlag == "T")
	{           
		//translate destination for sawtel
		Server.logInfo("CUSTOMER IS SAWTEL OR E164 DIALING ENABLED");
		if ( 0 == inputString.indexOf("011") )
		{
			Server.logInfo("DEST BEGIN WITH 011, STRIP 011, ADD + ");
			translatedString = "+" + translatedString.slice(3);
			Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
		}	
		else if ( 0 == inputString.indexOf("0") )
		{
			Server.logInfo("DEST BEGIN WITH 0, STRIP ZERO, ADD + ");
			translatedString = "+" + translatedString.slice(1);
			Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
		}
		else if ( '1' == inputString.charAt(0) )
		{
			Server.logInfo("DEST BEGIN WITH 1, ADD + ");
			translatedString = "+" + translatedString;
			Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
		}
		else if ( 0 == inputString.indexOf("+1") )
		{
			Server.logInfo("DEST BEGIN WITH +1, E.164 ALREADY DO NOTHING ");
			Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
		}
		else
		{
			Server.logInfo("STRIP NOTHING, ADD +1 ");
			translatedString = "+1" + translatedString;
			Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
		}
	}
	/********************** VERSCOM *******************************/	
	else if("verscom" == customer_code)
	{
		// translate destination for verscom
		Server.logInfo("CUSTOMER IS VERSCOM");
		if ( 0 == inputString.indexOf("00") )
		{
			Server.logInfo("DEST BEGIN WITH 00, STRIP OFF 00.");
			translatedString = translatedString.slice(2);
		}
		else if ( 0 == inputString.indexOf("0") )
		{
			Server.logInfo("DEST BEGIN WITH 00, STRIP OFF 0 AND ADD 90.");
			translatedString = "9" + translatedString;
		}
		/* 04/18/06 - removed at the request of Verscom. incident #060409-000001
		else if ( (0 == inputString.indexOf("17") || 0 == inputString.indexOf("36") || 0 == inputString.indexOf("39")) && 8 == inputString.length)
		{
			Server.logInfo("DEST BEGIN WITH 17, 36 OR 39 AND DEST LENGTH IS 8; ADD 973");
			translatedString = "973" + translatedString;
		} 
		*/
		else
		{
			Server.logInfo("NO RULES APPLY, LEAVE DEST AS IS.");			
		}
		Server.logInfo("TRANSLATED DEST IS:  " + translatedString);
	}
	else
	{
		Server.logInfo("DOES NOT MATCH ANY CUSTOMER, DO NOTHING.");
	}
	Server.logInfo("TRANSLATED DESTINATION IS: " + translatedString);
	
	return translatedString ;
	
}
/**
* Creates a PAI for an app.
*
* (X = anything, T=true, F=false, A= non-null and length>0 string, <null>=null or length=0 string)
*
* p_bFlag	p_strDefANI     p_strInPAI      p_strDestANI    PAI(output)
* T		A		X		X       	constructed from A
* T		<null>		X		X       	<null>
* F		X               A		X       	A
* F             X		<null>          A		constructed from A
* F             A               <null>          <null>          constructed from A
* F             <null>          <null>          <null>          <null>
*
* @param p_strCarrierName carrier's name (sawtel,latinode, etc)
* @param p_strDestANI destination ANI
* @param p_nCountryId country ID
* @param p_bOnNet on-net flag (true if call is on-net)
* @param p_strANI input ANI
* @param p_strHost host IP
* @param p_strInPAI input PAI
* @param p_bFlag flag (true to use default ANI always, 
*	false to use it only if p_strInPAI is blank and p_strDestANI is blank )
* @param p_strDefANI default ANI
* @return the generated PAI
*/
function js_create_app_PAI(p_strCarrierName,
	p_strDestANI,
	p_nCountryId,
	p_bOnNet,
	p_strANI,
	p_strHost,
	p_strInPAI,
	p_bFlag,
	p_strDefANI){

	Server.logInfo("IN js_create_app_PAI");
	Server.logInfo("p_bFlag: "+p_bFlag);
	Server.logInfo("p_strDefANI: "+p_strDefANI);
	Server.logInfo("p_strInPAI: "+p_strInPAI);
	Server.logInfo("p_strDestANI: "+p_strDestANI);
	var aniDest = "";
	var pai = null;
	if(p_bFlag == true){
		//always use default ani
		aniDest = p_strDefANI;
	}else if(p_strInPAI == null || p_strInPAI.length == 0){
		if(p_strDestANI == null || p_strDestANI.length == 0){
			aniDest = p_strDefANI;
		}else{
			aniDest = p_strDestANI;
		}
	}else{
		pai = p_strInPAI
	}
	if(aniDest.length > 0){
		var ani = js_translate_destination_broadband(p_strCarrierName,
					aniDest, 
					p_nCountryId,
					p_bOnNet, 
				 	p_strANI ) ;
		Server.logInfo("generating PAI from ANI <"+ani+">");
		pai = "<sip:" + ani + "@" + p_strHost + ">"; 
	}
	Server.logInfo("PAI IS: "+pai);
	return pai;
}