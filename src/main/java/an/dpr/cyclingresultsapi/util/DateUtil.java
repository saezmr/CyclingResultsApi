package an.dpr.cyclingresultsapi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.cyclingresultsapi.exception.CyclingResultsException;

public class DateUtil {
    
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    // DATE PATTERNS
    public static final String DDMMYYYY = "ddMMyyyy";
    public static final String DD_MM_YYYY_DOT = "dd.MM.yyyy";
    public static final String DD_MM_YYYY_DASH = "dd-MM-yyyy";
    public static final String DD_MM_YYYY_SLASH = "dd/MM/yyyy";
    public static final String DD_MM_YYYY_SPACE = "dd MM yyyy";
    public static final String MMDDYYYY = "MMddyyyy";
    public static final String MM_DD_YYYY_DOT = "MM.dd.yyyy";
    public static final String MM_DD_YYYY_DASH = "MM-dd-yyyy";
    public static final String MM_DD_YYYY_SLASH = "MM/dd/yyyy";
    public static final String MM_DD_YYYY_SPACE = "MM dd yyyy";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD_DOT = "yyyy.MM.dd";
    public static final String YYYY_MM_DD_DASH = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_SLASH = "yyyy/MM/dd";
    public static final String YYYY_MM_DD_SPACE = "yyyy MM dd";
    // TIME PATTERNS
    public static final String HHMM = "HHmm";
    public static final String HH_MM_COLON = "HH:mm";
    public static final String HH_MM_DOT = "HH.mm";
    public static final String HH_MM_SPACE = "HH mm";
    public static final String HHMMSS = "HHmmss";
    public static final String HH_MM_SS_COLON = "HH:mm:ss";
    public static final String HH_MM_SS_DOT = "HH.mm.ss";
    public static final String HH_MM_SS_SPACE = "HH mm ss";
    public static final String HHMMSS_SSS = "HHmmssSSS";
    public static final String HH_MM_SS_SSS_COLON = "HH:mm:ss:SSS";
    public static final String HH_MM_SS_SSS_DOT = "HH.mm.ss.SSS";
    public static final String HH_MM_SS_SSS_SPACE = "HH mm ss SSS";
    // SPECIAL PATTERNS
    public static final String DD_MMM_SPACE = "dd MMM";
    public static final String YYYYMMDD_HHMMSS_SSS = "yyyyMMddHHmmssSSS";
    public static final String DD_MMM_YYYY_SPACE = "dd MMM yyyy";
    public static final String JSON_DATE = "E, dd MMM yyyy HH:mm:ssZ";

    private static final Map<String, SimpleDateFormat> formatters = new HashMap<String, SimpleDateFormat>();
    private static boolean initialized = false;

    public enum FormatoJaxWS {
	XS_DATE, XS_DATE_TIME;
    }

    private DateUtil() {
    }

    private synchronized static void initialize() {
	if (!initialized) {
	    formatters.clear();
	    // DATE FORMATTERS
	    formatters.put(DDMMYYYY, new SimpleDateFormat(DDMMYYYY));
	    formatters.put(DD_MM_YYYY_DOT, new SimpleDateFormat(DD_MM_YYYY_DOT));
	    formatters.put(DD_MM_YYYY_DASH, new SimpleDateFormat(DD_MM_YYYY_DASH));
	    formatters.put(DD_MM_YYYY_SLASH, new SimpleDateFormat(DD_MM_YYYY_SLASH));
	    formatters.put(DD_MM_YYYY_SPACE, new SimpleDateFormat(DD_MM_YYYY_SPACE));
	    formatters.put(MMDDYYYY, new SimpleDateFormat(MMDDYYYY));
	    formatters.put(MM_DD_YYYY_DOT, new SimpleDateFormat(MM_DD_YYYY_DOT));
	    formatters.put(MM_DD_YYYY_DASH, new SimpleDateFormat(MM_DD_YYYY_DASH));
	    formatters.put(MM_DD_YYYY_SLASH, new SimpleDateFormat(MM_DD_YYYY_SLASH));
	    formatters.put(MM_DD_YYYY_SPACE, new SimpleDateFormat(MM_DD_YYYY_SPACE));
	    formatters.put(YYYYMMDD, new SimpleDateFormat(YYYYMMDD));
	    formatters.put(YYYY_MM_DD_DOT, new SimpleDateFormat(YYYY_MM_DD_DOT));
	    formatters.put(YYYY_MM_DD_DASH, new SimpleDateFormat(YYYY_MM_DD_DASH));
	    formatters.put(YYYY_MM_DD_SLASH, new SimpleDateFormat(YYYY_MM_DD_SLASH));
	    formatters.put(YYYY_MM_DD_SPACE, new SimpleDateFormat(YYYY_MM_DD_SPACE));
	    // TIME FORMATTERS
	    formatters.put(HHMM, new SimpleDateFormat(HHMM));
	    formatters.put(HH_MM_COLON, new SimpleDateFormat(HH_MM_COLON));
	    formatters.put(HH_MM_DOT, new SimpleDateFormat(HH_MM_DOT));
	    formatters.put(HH_MM_SPACE, new SimpleDateFormat(HH_MM_SPACE));
	    formatters.put(HHMMSS, new SimpleDateFormat(HHMMSS));
	    formatters.put(HH_MM_SS_COLON, new SimpleDateFormat(HH_MM_SS_COLON));
	    formatters.put(HH_MM_SS_DOT, new SimpleDateFormat(HH_MM_SS_DOT));
	    formatters.put(HH_MM_SS_SPACE, new SimpleDateFormat(HH_MM_SS_SPACE));
	    formatters.put(HHMMSS_SSS, new SimpleDateFormat(HHMMSS_SSS));
	    formatters.put(HH_MM_SS_SSS_COLON, new SimpleDateFormat(HH_MM_SS_SSS_COLON));
	    formatters.put(HH_MM_SS_SSS_DOT, new SimpleDateFormat(HH_MM_SS_SSS_DOT));
	    formatters.put(HH_MM_SS_SSS_SPACE, new SimpleDateFormat(HH_MM_SS_SSS_SPACE));
	    // SPECIAL PATTERNS
	    formatters.put(DD_MMM_SPACE, new SimpleDateFormat(DD_MMM_SPACE));
	    formatters.put(YYYYMMDD_HHMMSS_SSS, new SimpleDateFormat(YYYYMMDD_HHMMSS_SSS));
	    formatters.put(DD_MMM_YYYY_SPACE, new SimpleDateFormat(DD_MMM_YYYY_SPACE));
	    formatters.put(JSON_DATE, new SimpleDateFormat(JSON_DATE, new Locale("en_EN")));
	    // ALL DONE!
	    initialized = true;
	}
    }

    private static synchronized SimpleDateFormat getFormatter(String pattern) {
	SimpleDateFormat sdf = null;
	if (formatters.containsKey(pattern)) {
	    sdf = formatters.get(pattern);
	} else {
	    sdf = new SimpleDateFormat(pattern);
	    formatters.put(pattern, sdf);
	}
	return sdf;
    }

    /**
     * Method that takes a datetime object and returns it formatted using the
     * pattern provided. It can be used to get the <i>date</i> part or the
     * <i>time</i> part.
     * 
     * @param date
     *            <code>Date</code>
     * @param pattern
     *            <code>String</code>
     * @return <code>String</code>
     * @throws CyclingResultsException
     */
    public static String format(Date date, String pattern) throws CyclingResultsException {
	String result = null;
	if (date == null) {
	    throw new CyclingResultsException("Fecha a formatear en cadena no recibida.");
	} else if (pattern == null || pattern.isEmpty()) {
	    throw new CyclingResultsException("Patron para formatear fecha no recibido.");
	} else {
	    initialize();
	    SimpleDateFormat sdf = formatters.get(pattern);
	    if (sdf == null) {
		throw new CyclingResultsException("Patron para formatear fecha no reconocido: " + pattern);
	    } else {
		result = sdf.format(date);
	    }
	}
	return result;
    }

    /**
     * Method that takes a datetime object and returns it formatted using the
     * pattern provided. It will include both the <i>date</i> part and the
     * <i>time</i> part.
     * 
     * @param date
     *            <code>Date</code>
     * @param patternDate
     *            <code>String</code>
     * @param patternTime
     *            <code>String</code>
     * @return <code>String</code>
     * @throws CyclingResultsException
     */
    public static String format(Date date, String patternDate, String patternTime) throws CyclingResultsException {
	String result = null;
	if (date == null) {
	    throw new CyclingResultsException("Fecha a formatear en cadena no recibida.");
	} else if ((patternDate == null || patternDate.isEmpty()) || (patternTime == null || patternTime.isEmpty())) {
	    throw new CyclingResultsException("Patron para formatear fecha u hora no recibido.");
	} else {
	    initialize();
	    String fullPattern = patternDate + " " + patternTime;
	    SimpleDateFormat sdf = (formatters.containsKey(fullPattern)) ? formatters.get(fullPattern)
		    : getFormatter(fullPattern);
	    if (sdf == null) {
		throw new CyclingResultsException("Patron para formatear fecha y hora no reconocido: " + fullPattern);
	    } else {
		result = sdf.format(date);
	    }
	}
	return result;
    }

    /**
     * Method that takes a string and tries to parse it using the pattern
     * provided to get a date object without time. It should be used with a
     * pattern of type <i>date</i>, but not of type <i>time</i>.
     * 
     * @param date
     *            <code>String</code>
     * @param pattern
     *            <code>String</code>
     * @return <code>Date</code>
     * @throws CyclingResultsException
     */
    public static Date parse(String date, String pattern) throws CyclingResultsException {
	Date result = null;
	if (date == null || date.isEmpty()) {

	    throw new CyclingResultsException("Cadena a parsear en fecha no recibida.");
	} else if (pattern == null || pattern.isEmpty()) {
	    throw new CyclingResultsException("Patron para parsear fecha no recibido.");
	} else {
	    initialize();
	    SimpleDateFormat sdf = formatters.get(pattern);
	    if (sdf == null) {
		throw new CyclingResultsException("Patron para parsear fecha no reconocido: " + pattern);
	    } else {
		try {
		    result = sdf.parse(date);
		} catch (ParseException pe) {
		    throw new CyclingResultsException("Error al parsear fecha [" + date + "][" + pattern + "]: "
			    + pe.getMessage(), pe);
		}
	    }
	}
	return result;
    }

    /**
     * Method that takes a string and tries to parse it using the pattern
     * provided to get a date object with time.
     * 
     * @param date
     *            <code>String</code>
     * @param patternDate
     *            <code>String</code>
     * @param patternTime
     *            <code>String</code>
     * @return <code>Date</code>
     * @throws CyclingResultsException
     */
    public static Date parse(String date, String patternDate, String patternTime) throws CyclingResultsException {
	Date result = null;
	if (date == null || date.isEmpty()) {
	    throw new CyclingResultsException("Cadena a parsear en fecha y hora no recibida.");
	} else if ((patternDate == null || patternDate.isEmpty()) || (patternTime == null || patternTime.isEmpty())) {
	    throw new CyclingResultsException("Patron para parsear fecha u hora no recibido.");
	} else {
	    initialize();
	    String fullPattern = patternDate + " " + patternTime;
	    SimpleDateFormat sdf = (formatters.containsKey(fullPattern)) ? formatters.get(fullPattern)
		    : getFormatter(fullPattern);
	    if (sdf == null) {
		throw new CyclingResultsException("Patron para parsear fecha y hora no reconocido: " + fullPattern);
	    } else {
		try {
		    result = sdf.parse(date);
		} catch (ParseException pe) {
		    throw new CyclingResultsException("Error al parsear fecha y hora [" + date + "][" + fullPattern + "]: "
			    + pe.getMessage(), pe);
		}
	    }
	}
	return result;
    }

    /**
     * Method that takes a string and tries to parse it using the pattern
     * provided to get a XMLGregorianCalendar object without time. It should be
     * used with a pattern of type <i>date</i>, but not of type <i>time</i>.
     * 
     * @param date
     *            <code>String</code>
     * @param pattern
     *            <code>String</code>
     * @return <code>XMLGregorianCalendar</code>
     * @throws CyclingResultsException
     */
    public static XMLGregorianCalendar parseToXMLGregorianCalendar(String date, String patternDate)
	    throws CyclingResultsException {
	return parseToXMLGregorianCalendar(date, patternDate, FormatoJaxWS.XS_DATE);
    }

    /**
     * Method that takes a string and tries to parse it using the pattern
     * provided to get a XMLGregorianCalendar object without time. It should be
     * used with a pattern of type <i>date</i>, but not of type <i>time</i>.
     * 
     * @param date
     *            <code>String</code>
     * @param pattern
     *            <code>String</code>
     * @param formato
     *            <code>FormatoJaxWS</code> Formato en el que se mapea el obj
     *            XMLGregorianCalendar en soap
     * @return <code>XMLGregorianCalendar</code>
     * @throws CyclingResultsException
     */
    public static XMLGregorianCalendar parseToXMLGregorianCalendar(String date, String patternDate, FormatoJaxWS formato)
	    throws CyclingResultsException {
	try {
	    Date fecha = parse(date, patternDate);
	    GregorianCalendar gregorian = new GregorianCalendar();
	    gregorian.setTime(fecha);
	    return newXMLGregorianCalendar(gregorian, formato);
	} catch (Exception e) {
	    throw new CyclingResultsException("excepcion obteniendo xml gregorian calendar", e);
	}
    }

    /**
     * Method that takes a date and get a XMLGregorianCalendar object without
     * time.
     * 
     * @param date
     *            <code>java.util.Date</code>
     * @return <code>XMLGregorianCalendar</code>
     * @throws CyclingResultsException
     */
    public static XMLGregorianCalendar parseToXMLGregorianCalendar(Date fecha) throws CyclingResultsException {
	return parseToXMLGregorianCalendar(fecha, FormatoJaxWS.XS_DATE);
    }

    /**
     * Method that takes a date and get a XMLGregorianCalendar object without
     * time.
     * 
     * @param date
     *            <code>java.util.Date</code>
     * @param formato
     *            <code>FormatoJaxWS</code> Formato en el que se mapea el obj
     *            XMLGregorianCalendar en soap
     * @return <code>XMLGregorianCalendar</code>
     * @throws CyclingResultsException
     */
    public static XMLGregorianCalendar parseToXMLGregorianCalendar(Date fecha, FormatoJaxWS formato)
	    throws CyclingResultsException {
	try {
	    GregorianCalendar gregorian = new GregorianCalendar();
	    gregorian.setTime(fecha);
	    return newXMLGregorianCalendar(gregorian, formato);
	} catch (Exception e) {
	    throw new CyclingResultsException("", e);
	}
    }


    private static XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar gregorian, FormatoJaxWS formato)
	    throws DatatypeConfigurationException {
	XMLGregorianCalendar xmlGC;
	switch (formato) {
	case XS_DATE:
	    xmlGC = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(gregorian.get(Calendar.YEAR),
		    gregorian.get(Calendar.MONTH) + 1, gregorian.get(Calendar.DAY_OF_MONTH),
		    DatatypeConstants.FIELD_UNDEFINED);
	    break;
	case XS_DATE_TIME:
	    xmlGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorian);
	    break;
	default:
	    xmlGC = null;
	}
	return xmlGC;
    }
    
    public static Date dateWithoutHour(Date date) throws ParseException{
	SimpleDateFormat formatter = getFormatter(YYYYMMDD);
	String sdate = formatter.format(date);
	return formatter.parse(sdate);
    }
    
    public static Date firstDayOfMonth(Date date) throws ParseException{
	Calendar cal = Calendar.getInstance();
	cal.setTime(dateWithoutHour(date));
	cal.set(Calendar.DAY_OF_MONTH, 1);
	return cal.getTime();
    }

    public static Date lastDayOfMonth(Date date) throws ParseException{
	Calendar cal = Calendar.getInstance();
	cal.setTime(dateWithoutHour(date));
	cal.set(Calendar.DAY_OF_MONTH, 1);
	cal.add(Calendar.MONTH, 1);
	cal.add(Calendar.SECOND, -1);
	return cal.getTime();
    }
    
    public static Date firstDayOfYear(Date date) throws ParseException{
	Calendar cal = Calendar.getInstance();
	cal.setTime(dateWithoutHour(date));
	cal.set(Calendar.DAY_OF_YEAR, 1);
	return cal.getTime();
    }
    
    public static Date lastDayOfYear(Date date) throws ParseException{
	Calendar cal = Calendar.getInstance();
	cal.setTime(dateWithoutHour(date));
	cal.set(Calendar.DAY_OF_YEAR, 1);
	cal.add(Calendar.YEAR, 1);
	cal.add(Calendar.SECOND, -1);
	return cal.getTime();
    }
    

    public static Date parseUCIDate(String date) {
	SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyy", new Locale("en_EN"));
	try {
	    return sdf.parse(date);
	} catch (ParseException e) {
	    log.error("Error parseando " + date, e);
	    return null;
	}
    }
}