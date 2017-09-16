package ywcai.flow.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class MmSige {
	public static String getStringA(HashMap<String,String> parameters)
    {
		  List<HashMap.Entry<String, String>> list = new ArrayList<HashMap.Entry<String, String>>(parameters.entrySet());  
          // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）  
          Collections.sort(list, new Comparator<HashMap.Entry<String, String>>()  
          {

			@Override
			public int compare(Entry<String, String> o1, Entry<String, String> o2) {
				// TODO Auto-generated method stub
				return (o1.getKey()).toString().compareTo(o2.getKey().toString());  
			}   
          }); 
          String stringA="";
          for (HashMap.Entry<String, String> item : list)  
          {  
              if (StringUtils.isNotBlank(item.getValue())&&(!item.getValue().equals("-1")))  
              {  
                  stringA+=item.getKey()+"="+item.getValue()+"&";
              }  
          }  
          return stringA;
    }
}
