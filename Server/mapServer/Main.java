package mapServer;
import server.MultiServer;

/**
 * Lancia il sever su un porta indicata in input.
 */

public class Main {
	public static void main(String[] args) {
		System.out.println("Starting server...");
		new MultiServer(Integer.valueOf(args[0]));
	}
}