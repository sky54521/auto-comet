package org.auto.comet.support;

import javax.servlet.ServletContext;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.auto.comet.ObjectFactory;
import org.auto.comet.config.CometConfigMetadata;

/**
 * @author XiaohangHu
 * */
public class ObjectFactoryBuilder {

	public static ObjectFactory creatObjectFactory(
			CometConfigMetadata cometConfig, ServletContext servletContext) {
		String className = cometConfig
				.getProperty(CometConfigMetadata.OBJECT_FACTORY_PROPERTY_NAME);

		if (StringUtils.isBlank(className)) {
			return creatDefaultObjectFactory(servletContext);
		}
		return creatObjectFactory(className, servletContext);
	}

	private static ObjectFactory creatObjectFactory(String className,
			ServletContext servletContext) {

		Object object = newInstance(className);

		if (object instanceof ObjectFactory) {
			ObjectFactory factory = (ObjectFactory) object;
			factory.init(servletContext);
			return factory;
		} else {
			throw new IllegalStateException("ObjectFactory must implements ["
					+ ObjectFactory.class.getName() + "]");
		}
	}

	private static Object newInstance(String className) {
		return newInstance(getClass(className));
	}

	private static Class<?> getClass(String className) {
		try {
			return ClassUtils.getClass(className);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Class not fonud [" + className
					+ "]", e);
		}
	}

	private static Object newInstance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Is it an abstract class["
					+ clazz.getName() + "]?", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(
					"the constructor can't access by class[" + clazz.getName()
							+ "]", e);
		}
	}

	protected static ObjectFactory creatDefaultObjectFactory(
			ServletContext servletContext) {
		ObjectFactory objectFactory = new ClassNameObjectFactory();
		objectFactory.init(servletContext);
		return objectFactory;
	}
}
