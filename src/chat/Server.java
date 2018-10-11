package chat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Server extends Thread 
{
	private static ArrayList<BufferedWriter>clients;           
	private static ServerSocket server; 
	private String name;
	private Socket con;
	private InputStream in;  
	private InputStreamReader inr;  
	private BufferedReader bfr;
	
	public Server (Socket con) 
	{
		this.con = con;
		try 
		{
			in = con.getInputStream();
			inr = new InputStreamReader(in);
			bfr = new BufferedReader(inr);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run ()
	{
		try
		{
			String msg;
			OutputStream ou = this.con.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfw = new BufferedWriter(ouw);
			clients.add(bfw);
			name = msg = bfr.readLine();
			
			while(!"exit".equalsIgnoreCase(msg) && msg != null)
			{
				msg = bfr.readLine();
				sendToAll(bfw, msg);
				System.out.println(msg);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendToAll (BufferedWriter bwOut, String msg) throws IOException
	{
		BufferedWriter bwO;
		
		for(BufferedWriter bw : clients )
		{
			bwO = (BufferedWriter)bw;
			if(!(bwOut == bwO))
			{
				bw.write(name + ": " + msg + "\r\n");
				bw.flush();
			}
		}
	}
	
	public static void main (String []args)
	{
		try 
		{
		    JLabel lblMessage = new JLabel("Server Door:");
		    JTextField txtDoor = new JTextField("12345");
		    Object[] texts = {lblMessage, txtDoor };  
		    JOptionPane.showMessageDialog(null, texts);
		    server = new ServerSocket(Integer.parseInt(txtDoor.getText()));
		    clients = new ArrayList<BufferedWriter>();
		    JOptionPane.showMessageDialog(null,"Active server on door: "+         
		    txtDoor.getText());
		    
		    while(true)
		    {
		        System.out.println("Waiting for connection...");
		        Socket con = server.accept();
		        System.out.println("Client connected...");
		        Thread t = new Server(con);
		        t.start();
		    }  
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}