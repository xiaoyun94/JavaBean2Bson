package cn.bigforce.bean2bson;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.print.Doc;

import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.bson.BsonValue;


/**
 * You have to pay attention to inheritance case! If you assign a subclass
 * instance to a superclass declaration such as A a = new B();where B extends A
 * The BSON document will be generated as the properties of real class of
 * instance with more properties. However, the process will be irreversible,
 * Because java bean will be generated only as the properties of declaration.
 * 
 * @author bigforce
 * @Date aaa
 */
public class Bean2Bson {
	/**
	 * analysis java bean
	 * @param struc a java instance
	 * @return a BsonDocument which can be used for mongodb
	 */
	public static BsonDocument analysisInstance(Object struc) {
		Class baseClass = struc.getClass();
		
		BsonDocument document = new BsonDocument();
		
		for (Class localClass = baseClass; localClass != Object.class; localClass = localClass.getSuperclass()) {
			Field[] fields = localClass.getDeclaredFields();
			for (Field field : fields) {
				String getterName = getGetterName(field.getName());
				
				try {
					Method method = baseClass.getMethod(getterName);
					Object object = method.invoke(struc);
					BsonValue value = toBsonValue(object);
					document.put(field.getName(), value);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}

		return document;
	}

	/**
	 * Transform to the type that is acceptable for BSON
	 * 
	 * @param object
	 * @return
	 */
	private static BsonValue toBsonValue(Object object) {
		if (object == null) {
			return new BsonNull();
		} else if (object.getClass().isArray()) {
			// Array needs a iteration to transform every element
			Object[] objects = (Object[]) object;
			return toBsonArray(Arrays.asList(objects));
		} else if (object instanceof Collection) {
			// List needs a iteration to transform every element
			return toBsonArray((List<Object>) object);
		} else if (object instanceof Map) {
			// Map needs a iteration to transform every value element
			BsonDocument document = new BsonDocument();
			Map<String, Object> map = (Map<String, Object>) object;
			for (String key : map.keySet()) {
				document.put(key, toBsonValue(map.get(key)));
			}
			return document;
		}
		// these are basic element which BSON supports and this is the
		// destination of recursion
		else if (object instanceof String) {
			return new BsonString(object.toString());
		} else if (object instanceof Date) {
			return new BsonDateTime(((Date)object).getTime());
		}else if (object instanceof Float) {
			return new BsonDouble((float)object);
		}else if (object instanceof Double) {
			return new BsonDouble((double)object);
		} else if (object instanceof Boolean) {
			return new BsonBoolean((boolean)object);
		} else if (object instanceof Integer) {
			return new BsonInt32((int)object);
		} else if (object instanceof Long) {
			return new BsonInt64((long)object);
		} else if (object instanceof Character) {
			return new BsonString(object.toString());
		} else if (object instanceof Short) {
			return new BsonInt32((short)object);
		} else if (object instanceof Byte) {
			return new BsonInt32((byte)object);
		}else {
			return analysisInstance(object);
		}
	}

	private static BsonArray toBsonArray(List<Object> list) {
		BsonArray array = new BsonArray();
		for (Object object : list) {
			BsonValue value = toBsonValue(object);
			if(value!=null)
				array.add(value);
		}
		return array;
	}

	private static String getGetterName(String declaredFieldName) {
		return "get" + declaredFieldName.substring(0, 1).toUpperCase() + declaredFieldName.substring(1);

	}


}
