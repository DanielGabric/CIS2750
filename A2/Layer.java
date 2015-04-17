/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 2
*/

public class Layer {
    static{
        System.loadLibrary("JNI_Layer");
    }

    public native void create(int size);
    public native void destroy();
    public native boolean parseFrom(String path);
    public native boolean manage(String pname,int ptype, int required);
    public native boolean hasValue(String pname);
    public native float getFloat(String pname);
    public native boolean getBool(String pname);
    public native String getStr(String pname);
    public native String[] getList(String pname);
    public native int getInt(String pname);

    public Layer(int size){
        create(size);
    }
    public void destroyPM(){
        destroy();
    }
    public static void main (String[]args){
        
        Layer l = new Layer(30);
        l.manage("title",3,1);
        l.manage("fields",4,1);

        l.parseFrom("testcase.config");
        String[]hey = l.getList("fields");
        for(int i=0;i<hey.length;++i){
            System.out.println(hey[i]);
        }
        System.out.println(l.getStr("title"));
        
        
    }
}
