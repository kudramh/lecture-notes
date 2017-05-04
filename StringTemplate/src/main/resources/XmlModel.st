// file XML Model
XmlModel(packageName, class, fields) ::=  <<

package <packageName>;

import com.softtek.legacy.framework.model.DataElement.DataType;
import com.softtek.legacy.framework.model.EntityModel;

public class <class> {

	<fields:{ map | 
		/* <map.description> */
		@EntityModel(index = 0, value = "", name="<map.fieldName>", type=DataType.<map.fieldType>, length=<map.length>, decimales = 0, format = "")
		private <map.fieldType> <map.fieldName>;

	}>
	<fields:{ map | 
		/* <map.description> */	
		public <map.methodType> get<map.methodName>(){
			return this.<map.fieldName>;
		\}
		public void set<map.methodName>(<map.methodType> <map.fieldName>){
			this.<map.fieldName> = <map.fieldName>;
		\}

	}>
}
>>