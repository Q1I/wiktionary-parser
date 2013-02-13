import java.util.HashMap;


public class Map extends HashMap<String, String>{

	// uri 		, label
	// uriLang	, label
	
	public Map(){
		super();
	}
	
	public void add(String key, String value){
		if(this.size()>80){
			this.put(key, value);
		}
	}
	
	public void set(String uri , String label){
//		if(this.size() > 80){
//			this.k
//		}
	}
}
