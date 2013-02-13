import java.util.ArrayList;


public class Resource {

	private String uri=null;
	private String hasLangUsage=null;
	private String language=null;
	
	private String hasPoSUsage=null;
	private ArrayList<String> hasPoS=null;
	private String hasHyphenationSingular=null;
	private String hasHyphenationPlural=null;
	
	private String hasEtymology=null;
	private ArrayList<String> hasSense=null;
	private ArrayList<String> hasMeaning=null;
	private ArrayList<String> hasExampleSentence=null;
	private String label=null;
	
	private ArrayList<String> hasProverb = null;
	private ArrayList<String> hasTranslation = null;
	private String creator = null;
	private String seeAlso = null;
	private String type = null;
	

	
	public Resource(){
		this.hasPoS=new ArrayList<String>();
		this.hasSense=new ArrayList<String>();
		this.hasMeaning=new ArrayList<String>();
		this.hasExampleSentence=new ArrayList<String>();
		this.hasProverb=new ArrayList<String>();
	}
	
	public void reset(){

		this.uri=null;
		this.hasLangUsage=null;
		this.language=null;
		this.hasPoSUsage=null;
//		this.hasPoS=null;
		this.hasHyphenationSingular=null;
		this.hasEtymology=null;
//		this.hasSense=null;
//		this.hasMeaning=null;
//		this.hasExample=null;
//		this.label=null;
		this.hasPoS=new ArrayList<String>();
		this.hasSense=new ArrayList<String>();
		this.hasMeaning=new ArrayList<String>();
		this.hasExampleSentence=new ArrayList<String>();
		
	}

	public String getUri() {
		return this.uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}


	public String getHasLangUsage() {
		return hasLangUsage;
	}

	public void setHasLangUsage(String hasLangUsage) {
		this.hasLangUsage = hasLangUsage;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHasPoSUsage() {
		return hasPoSUsage;
	}

	public void setHasPoSUsage(String hasPoSUsage) {
		this.hasPoSUsage = hasPoSUsage;
	}

	public ArrayList<String> getHasPoS() {
		return hasPoS;
	}

	public void addHasPoS(String hasPoS) {
		for(int i=0; i<this.hasPoS.size();i++){
			if(this.hasPoS.get(i).equals(hasPoS))
				return;
		}
		this.hasPoS.add(hasPoS);
	}

	public String getHasHyphenationSingular() {
		return hasHyphenationSingular;
	}

	public void setHasHyphenationSingular(String hasHyphenationSingular) {
		this.hasHyphenationSingular = hasHyphenationSingular;
	}

	public ArrayList<String> getHasSense() {
		return hasSense;
	}

	public void addHasSense(String hasSense) {
		this.hasSense.add(hasSense);
	}

	public ArrayList<String> getHasMeaning() {
		return hasMeaning;
	}

	public void addHasMeaning(String hasMeaning) {
		this.hasMeaning.add(hasMeaning);
	}

	public ArrayList<String> getHasExample() {
		return hasExampleSentence;
	}

	public void addHasExample(String hasExample) {
		this.hasExampleSentence.add(hasExample);
	}

	public void setLabel(String label) {
		this.label=label;
	}
	public String getLabel() {
		return this.label;
	}

	public void setHasEtymology(String hasEtymology) {
		this.hasEtymology=hasEtymology;
		
	}
	public String getHasEtymology(){
		return this.hasEtymology;
	}

	public String getHasHyphenationPlural() {
		return hasHyphenationPlural;
	}

	public void setHasHyphenationPlural(String hasHyphenationPlural) {
		this.hasHyphenationPlural = hasHyphenationPlural;
	}

	public ArrayList<String> getHasProverb() {
		return hasProverb;
	}

	public void addHasProverb(String hasProverb) {
		this.hasProverb.add(hasProverb);
	}
}
