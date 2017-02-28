import edu.wpi.first.wpilibj.networktables.*;

public class Main {

	public static void main(String[] args) {
		//loads opencv for vision processing.
		//System.loadLibrary("opencv_java310");
		
		NetworkTable.setTeam(5899);
	    NetworkTable publishingTable = NetworkTable.getTable("dataTable");
	    NetworkTable.initialize();
	    
	    publishingTable.delete("test1");
	    
		double x = 0;
		
		while (true) {
			publishingTable.putNumber("test2", x);
			System.out.println("hello");
			x+=1;
		}

	}

}
