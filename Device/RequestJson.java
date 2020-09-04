public class RequestJson {
    private Map<String,String> paramMap;

    public RequestJson() {
        this.paramMap = new HashMap<>();
    }

    public void add(String key, String value){
        paramMap.put(key,value);
    }

    public String toJsonString(){
        StringBuilder stringBuilder = new StringBuilder("{");
        List<String> params = new ArrayList<>();
        for(Map.Entry entry : this.paramMap.entrySet()){
            String aParam = "\"" + entry.getKey() +
                    "\" : \"" +
                    entry.getValue() +
                    "\"";
            params.add(aParam);
        }
        stringBuilder.append(String.join(",",params));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
