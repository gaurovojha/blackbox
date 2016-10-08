/**
 *
 */
package com.blackbox.ids.ui.tags;

import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.blackbox.ids.ui.common.Constants;

/**
 * Superclass for all Blackbox tags that require a RequestContext.
 *
 * The RequestContext instance provides easy access to current state like the org.springframework.web.context.WebApplicationContext,
 * the java.util.Locale, the org.springframework.ui.context.Theme, etc.
 * <p/>
 * All request context aware tags must extend this BaseTag.
 *
 * @author ajay2258
 *
 */
public abstract class BaseTag extends RequestContextAwareTag {

	/** The serial version UID. */
	private static final long serialVersionUID = 2228126351135553978L;

	/** The id instance */
    private String id;

    /** The name instance to map the path */
    private String name;

    /** The pre- selected value */
    private Long value;

    /** The pre-selected value for phone code */
    private String codeValue;

    /** The error class to bind the dynamic error from the form */
    private String cssErrorClass;

	@Override
	protected abstract int doStartTagInternal();

	/*- ------------------------------------------ Utils Methods -- */
	/**
     * Method which accepts the jspContext and returns the applicationContext instance to retrieve the bean at child
     * class.
     *
     * @return the instance of {@link ApplicationContext}
     */
    public ApplicationContext getContext() {
        return getRequestContext().getWebApplicationContext();
    }

	/**
     * Method fetches the <code>MessageSource</code> bean from the application context.
     *
     * @return The reload able resource bundle message source.
     */
    public MessageSource getMessageSource() {
        return (MessageSource) getContext().getBean(Constants.BEAN_MESSAGE_SOURCE);
    }

    /**
     * Returns the locale specified in the servlet response using the setLocale method.
     *
     * @return Locale the preferred locale for the servlet response.
     */
    public Locale getLocale() {
        final Locale responseLocale = getRequestContext().getLocale();
        return responseLocale;
    }

    /*- ---------------------------- getter-setters -- */
    @Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public String getCssErrorClass() {
		return cssErrorClass;
	}

	public void setCssErrorClass(String cssErrorClass) {
		this.cssErrorClass = cssErrorClass;
	}

}
