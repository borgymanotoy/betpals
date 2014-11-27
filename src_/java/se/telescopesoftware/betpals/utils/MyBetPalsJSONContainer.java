package se.telescopesoftware.betpals.utils;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

/*
 * -------------
 * Sample Code
 * -------------
 * 
 * MyBetPalsJSONContainer con = new MyBetPalsJSONContainer();

	con.addToArray("data0");
	con.addToArray("data1");
	con.addToArray("data2");
	con.addToArray("data3");
	
	ArrayList alist = new ArrayList();
	alist.add("d1");
	alist.add("d2");
	
	con.addToArray(alist);
	
	con.addObject("FNAME", "Juan");
	con.addObject("LNAME", "dela Cruz");
	
	ArrayList competitions = new ArrayList();
	competitions.add("AA");
	competitions.add("BB");
	competitions.add("CC");
	competitions.add("DD");
	
	con.addObject("COMPETITIONS", competitions);
 * 
 * -----------------
 * JSON RESULT:
 * -------------------
 * {
	"array":["data0","data1","data2","data3",["d1","d2"]],
	"message":"",
	"objects":{
		"FNAME":"Juan",
		"LNAME":"dela Cruz",
		"COMPETITIONS":["AA","BB","CC","DD"]},
	"status":"success"
	}
 * 
 * 
 * ---------------------
 * JAVA SCRIPT
 * ----------------------
 * 
	function loadJSONData(jsondata)
	{
		alert("TEST JSON DATA");
		alert('status=>'+jsondata.status);
		alert('message=>'+jsondata.message);
		
		var arr = jsondata.array;
		alert('array=>'+arr);
		for(var i = 0; i < arr.length; i++)
		{
			alert("array["+i+"]=>"+arr[i]);
		}
		
		var objs = jsondata.objects;
		alert('FNAME=>'+objs.FNAME );
		alert('LNAME=>'+objs.LNAME );
		var competitions = objs.COMPETITIONS;
		
		alert("COMPETITIONS=>"+competitions);
	}
 */

public class MyBetPalsJSONContainer {

	private String status = STATUS_SUCCESS;
	private String message;
	private JSONArray array;
	private JSONObject objects;
	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_FAILED = "failed";

	public MyBetPalsJSONContainer()
	{
		array = new JSONArray();
		objects = new JSONObject();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public JSONArray getArray() {
		return array;
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	public JSONObject getObjects() {
		return objects;
	}

	public void setObjects(JSONObject objects) {
		this.objects = objects;
	}

	public void addObject(String key, Object value)
	{
		objects.element(key, value);
	}

	public Object removeObject(String key)
	{
		return objects.remove(key);
	}

	public String toJSON() { 
		return JSONObject.fromObject(this).toString(); 
	}

	public void addToArray(int index, Object element) {    
		array.add(index, element);
	}

	public String toString()
	{
		return toJSON();
	}

	public JSONObject convertFromHashMap(HashMap hMap) {
		JSONObject jObject = new JSONObject();
		if(hMap != null) {
			Set st = hMap.keySet();
			Iterator itr = st.iterator();
			while (itr.hasNext()) {
				String keyValue = (String) itr.next();
				System.out.println(keyValue);
				Object objectValue = hMap.get(keyValue);
				jObject.element(keyValue, objectValue);
			}
		}
		return jObject;
	}

	public JSONArray convertFromObjectArrayList(ArrayList results) {
		JSONArray jArray = new JSONArray();
		for (Object o : results) {
			jArray.add(o);
		}
		return jArray;
	}
}
