package org.apache.commons.jelly.tags.swing;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import java.awt.Component;
import java.awt.Container;

import org.apache.commons.beanutils.ConvertUtils;

import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.TagLibrary;
import org.apache.commons.jelly.impl.TagScript;
import org.apache.commons.jelly.impl.TagFactory;
import org.apache.commons.jelly.tags.swing.converters.DimensionConverter;
import org.apache.commons.jelly.tags.swing.converters.PointConverter;

import org.apache.commons.jelly.DynaBeanTagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.beanutils.BeanUtils;

import org.apache.commons.jelly.impl.TagFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.xml.sax.Attributes;

/** This class represents a layout-manager constraints as passed in
	* the second argument of {@link Container#add(Component,Object)}.
	*	<p>
	*	In essence, it looks really like nothing else than a bean-class...
	*	with {@link #getConstraintObject}.
	*	Probably a shorter java-source is do-able.
	*	<p>
	*	TODO: this class should probably be extended with special treatment for dimensios
	*	using the converter package.
	*/
public class ConstraintTag extends DynaBeanTagSupport {
	
/*	TODO: make a gridbagconstraintTag class which supports an attribute "parent"
						(or... startWith) which is another gridbagconstraintTag whose gridbagconstraint
						is cloned then attributes are set
						This tag should also support the attributes such as fill=BOTH
						and anchor=NORTHEAST...
						Wooops... need to define setters ?? let's see if BeanUtils does it on public vars
						And... have an insets?? A child ?
	*/

	protected Factory factory;	
	protected String var = null;
	protected Object bean = null;

		public static class HereFactory extends BeanFactory implements TagFactory {
			public HereFactory(Class c) { super(c); }
			public Tag createTag(String name, Attributes attributes) {
				return new ConstraintTag ( this );
				// still scratching my head about "this" usage...
			}
		} // class HereFactory
		public static class ConstantFactory implements TagFactory, Factory {
			public ConstantFactory(Object c) { this.constant = c;}
			private Object constant;
			public Object newInstance() { return constant; }
			public Tag createTag(String name, Attributes attributes) throws Exception {
				return new ConstraintTag ( this );
			}
		} // class ConstatnStringFactory

		// we could be able to make factories that create their tags in parametrized
		// subclasses of the tag depending on the name and attributes
		// it would useful, for example, to make a cardLayout's <card name="">
	
	
	public ConstraintTag (Factory factory) {
		this.factory = factory;
	}
	
	public void createBean ( Factory factory ) throws Exception {
		bean = factory.newInstance();
	}
	
	// --------------------------------------------- ATTRIBUTES
	
	public void beforeSetAttributes (  ) throws Exception {
		createBean(factory);
	}
	
	
	public void setAttribute ( String name, Object value ) throws Exception {
		// no real need for DynaBeans or ?
		if ( "var".equals(name) )
			var = value.toString();
		else BeanUtils.setProperty( bean, name, value );
	}
// --------------------------------------------------	
	/** Children invocation... just nothing...
		*/
	public void doTag ( XMLOutput output ) throws Exception {
		if ( var != null ) context.setVariable ( var, getBean() );
		invokeBody ( output );
		// nothing else to do... the getConstraintObject method should have been called.
	}
	
	// ----------------------------------------------
	public Object getBean() {
		return bean;
	}
	
	/** Returns the attached constraint object.
		*/
	public Object getConstraintObject() {
		return getBean();
	}
} // class ConstraintTag