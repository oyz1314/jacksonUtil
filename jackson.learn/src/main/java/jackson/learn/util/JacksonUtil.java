package jackson.learn.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jackson.learn.enums.SexEnum;
import jackson.learn.pojo.Person;

public class JacksonUtil {

	private static String objectToJson(ObjectMapper objectMapper,Object obj) {
		String jsonStr = null;
		try {
			jsonStr = objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	
	private static<T> T jsonToObject(ObjectMapper objectMapper,String jsonStr,Class<T> classInfo) {
		T obj = null;
		try {
			obj = objectMapper.readValue(jsonStr, classInfo);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static void main(String[] args) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		//json转java对象
//		// 去掉默认的时间戳格式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//		// 设置为中国上海时区
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//		objectMapper.setTimeZone(TimeZone.getDefault());
//		// 序列化时，日期的统一格式
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

//		// 将枚举以toString()输出
		objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		//将枚举以下标输出
//		objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false);

//

		Person person = new Person();
		person.setName("Jack");
		person.setAge(21);
		person.setSex(SexEnum.MAN);

		String dateStr = "2012-06-18 11:11:11";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		person.setWorkFromDay(sdf.parse(dateStr));
//		System.out.println(objectToJson(objectMapper,person));






		List<Person> personList = new ArrayList<Person>();
		personList.add(person);
		Person personTwo = new Person();
		personTwo.setName("Lee");
		personTwo.setAge(28);
		personTwo.setSex(SexEnum.MAN);
		//personList.add(personTwo);
		//将单列表以普通对象方式输出
		objectMapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, true);
//		System.out.println(objectToJson(objectMapper,personList));



//		char[] countChar = new char[] { 'a', 'b', 'c', 'd' };
//		为true时，将字符数组以jsonArray方式输出
//		objectMapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false);
//		System.out.println(objectToJson(objectMapper,countChar));
//		objectMapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
//		System.out.println(objectToJson(objectMapper,countChar));
		//输出根元素
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

//		System.out.println(objectToJson(objectMapper,person));



		//对输出的格式进行优化
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

//		System.out.println(objectToJson(objectMapper,person));




//		personTwo.setName(null);
		personTwo.setName("jack");
		personTwo.setAge(0);
//		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//属性为null时不序列化
//		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);//属性为null或""时不序列化
//		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);//属性为null或""或0时不序列化
		objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);//默认都序列化
//		System.out.println(objectToJson(objectMapper,personTwo));
//

		//json转java对象
		String personStr = "{\"name\":\"amanda\",\"age\":29,\"workFromDay\":\"2012-09-10 09:00:00\",\"sex\":\"MAN\"}";
//		System.out.println(jsonToObject(objectMapper,personStr,Person.class));

		personStr = "{\"name\":\"amanda\",\"age\":29,\"workFromDay\":\"2012-09-10 09:00:00\",\"sex\":\"MAN\",\"name1\":\"amanda1\"}";//不存在的key
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		System.out.println(jsonToObject(objectMapper,personStr,Person.class));

		personStr = "{\"name\":\"amanda\",\"age\":29,\"workFromDay\":\"2012-09-10 09:00:00\",sex:\"MAN\"}";//key没有双引号
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//		System.out.println(jsonToObject(objectMapper,personStr,Person.class));

		personStr = "{\"name\":\"amanda\",\"age\":0029,\"workFromDay\":\"2012-09-10 09:00:00\",sex:\"MAN\"}";//value以0开头
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
//		System.out.println(jsonToObject(objectMapper,personStr,Person.class));


		personStr = "{\"name\":\"amanda\",\"age\":0029,\"workFromDay\":,sex:\"MAN\"}";//漏掉了一个属性值
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_MISSING_VALUES, true);
//		System.out.println(jsonToObject(objectMapper,personStr,Person.class));

		personStr = "{\"name\":'amanda',\"age\":0029,\"workFromDay\":\"2012-09-10 09:00:00\",sex:\"MAN\"}";//有单引号
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
//		System.out.println(jsonToObject(objectMapper,personStr,Person.class));

		personStr = "{\"name\":'amanda',\"name\":\"lee\",\"age\":0029,\"workFromDay\":\"2012-09-10 09:00:00\",sex:\"MAN\"}";//重复的key
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.STRICT_DUPLICATE_DETECTION, true);
//		System.out.println(jsonToObject(objectMapper,personStr,Person.class));

		personStr = "{\"nameInfo\":'amanda',\"age\":0029,\"workFromDay\":\"2012-09-10 09:00:00\",sex:\"MAN\"}";//重命名
//		System.out.println(jsonToObject(objectMapper,personStr,Person.class));

		personStr = "{\"nameInfo\":'amanda',\"age\":0029,\"workFromDay\":\"2012-09-10 09:00:00\",sex:\"MAN\"}";//忽略属性
//		System.out.println(jsonToObject(objectMapper,personStr,Person.class));


		String personListStr = "[{\"nameInfo\":\"amanda\",\"age\":0029,\"workFromDay\":\"2012-09-10 09:00:00\",sex:\"MAN\"},{\"nameInfo\":\"jenny'\",\"age\":0032,\"workFromDay\":\"2008-06-30 09:00:00\",sex:\"WOMAN\"}]";//复杂对象
		System.out.println(jsonToObject(objectMapper,personListStr,List.class));
/*		System.out.println(objectMapper.readValue(personListStr,new TypeReference<List<Person>>() {
		}));*/


	}

}
