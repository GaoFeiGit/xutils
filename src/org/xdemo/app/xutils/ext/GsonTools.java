package org.xdemo.app.xutils.ext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * Gson封装
 * 
 * @author Goofy 252887950@qq.com <a href="http://www.xdemo.org">www.xdemo.org</a>
 * @Date 2017年8月18日 下午3:29:20
 */
public class GsonTools {

	/**
	 * 对象转JSON
	 * @param obj
	 * @return
	 */
	public static String toJson(Object obj) {
		return new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(obj);
	}
	
	/**
	 * 对象转JSON
	 * @param obj 
	 * @param excludes 过滤掉的字段
	 * @return
	 */
	public static String toJson(Object obj,final String...excludes){
		return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
			public boolean shouldSkipField(FieldAttributes f) {
				for(String ex:excludes){
					if(ex.equals(f.getName()))return true;
				}
				return false;
			}
			
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		}).serializeNulls().create().toJson(obj);
	}

	/**
	 * 对象转JSON
	 * @param obj
	 * @param dateFormat 日期格式
	 * @return
	 */
	public static String toJson(Object obj, String dateFormat) {
		return new GsonBuilder().serializeNulls().setDateFormat(dateFormat).create().toJson(obj);
	}
	
	/**
	 * 对象转JSON
	 * @param obj
	 * @param dateFormat 日期格式
	 * @param excludes 排除的字段
	 * @return
	 */
	public static String toJson(Object obj,String dateFormat,final String...excludes){
		return new GsonBuilder().setDateFormat(dateFormat).setExclusionStrategies(new ExclusionStrategy() {
			public boolean shouldSkipField(FieldAttributes f) {
				for(String ex:excludes){
					if(ex.equals(f.getName()))return true;
				}
				return false;
			}
			
			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}
		}).serializeNulls().create().toJson(obj);
	}

	public static JsonObject parse(String json) {
		JsonParser parser = new JsonParser();
		return parser.parse(json).getAsJsonObject();
	}

	public static <T> T jsonToEntity(String json, Class<T> clazz) {
		return new Gson().fromJson(json, clazz);
	}

	public static <T> List<T> jsonToList(String json, Class<T> clazz) {
		Gson gson = new Gson();
		List<T> list = new ArrayList<T>();
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		for (final JsonElement elem : array) {
			list.add(gson.fromJson(elem, clazz));
		}
		return list;
	}

	public static <K, V> Map<K, V> jsonToMap(String gsonString) {
		Map<K, V> map = null;
		Gson gson = new Gson();
		map = gson.fromJson(gsonString, new TypeToken<Map<K, V>>() {}.getType());
		return map;
	}
	
	
}
