import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.print.*;
import java.net.*;

class notepad1 extends JFrame implements ActionListener,Printable
{
	int i;
	JFrame jf;
	JTextArea ta=new JTextArea(5,30);;
	JMenuBar mb;
	JMenu menu[];
	JMenuItem filemenuitem[],editmenuitem[];
	JMenuItem font,viewhelp;
	JCheckBoxMenuItem status_bar;
	JDesktopPane jp;
	JFileChooser jfc;
	PrinterJob pj;
	Container b;
	String path,original1,selected_text1,final_text1;

	notepad1()
	{
		jf=new JFrame("Notepad");
		mb=new JMenuBar();
		menu=new JMenu[5];
		filemenuitem=new JMenuItem[7];
		editmenuitem=new JMenuItem[5];
		font=new JMenuItem("Font...");
		viewhelp=new JMenuItem("View Help");
		status_bar=new JCheckBoxMenuItem("Status Bar");
		jp=new JDesktopPane();
		jfc=new JFileChooser("E://practice/");

		String s1[]={"File","Edit","Format","View","Help"};
		String s2[]={"New","Open...","Save","Save_as..","Page Setup...","Print...","Exit"};
		String s3[]={"Undo","Cut","Copy","Paste","Delete"};
		for(i=0;i<menu.length;i++)
			menu[i]=new JMenu(s1[i]);
		for(i=0;i<filemenuitem.length;i++)
			filemenuitem[i]=new JMenuItem(s2[i]);
		for(i=0;i<editmenuitem.length;i++)
			editmenuitem[i]=new JMenuItem(s3[i]);
		
		int s4[]={KeyEvent.VK_F,KeyEvent.VK_E,KeyEvent.VK_R,KeyEvent.VK_V,KeyEvent.VK_H};
		int s5[]={KeyEvent.VK_N,KeyEvent.VK_O,KeyEvent.VK_S,KeyEvent.VK_P};
		int s6[]={KeyEvent.VK_Z,KeyEvent.VK_X,KeyEvent.VK_C,KeyEvent.VK_V,KeyEvent.VK_DELETE};

		jf.add(ta);
		jf.setJMenuBar(mb);
		for(i=0;i<menu.length;i++)
		{	mb.add(menu[i]);
			menu[i].addActionListener(this);
		}
		for(i=0;i<filemenuitem.length;i++)
		{	menu[0].add(filemenuitem[i]);
			filemenuitem[i].addActionListener(this);
		}	
		for(i=0;i<editmenuitem.length;i++)
		{	menu[1].add(editmenuitem[i]);
			editmenuitem[i].addActionListener(this);
		}	
		menu[2].add(font);
		menu[3].add(status_bar);
		menu[4].add(viewhelp);

		for(i=0;i<5;i++)
			menu[i].setMnemonic(s4[i]);
		for(i=0;i<3;i++)
			filemenuitem[i].setAccelerator(KeyStroke.getKeyStroke(s5[i],ActionEvent.CTRL_MASK));
		filemenuitem[5].setAccelerator(KeyStroke.getKeyStroke(s5[3],ActionEvent.CTRL_MASK));
		for(i=0;i<5;i++)
			editmenuitem[i].setAccelerator(KeyStroke.getKeyStroke(s6[i],ActionEvent.CTRL_MASK));
		jf.setSize(1370,1370);
		b= getContentPane();
		font.addActionListener(this);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//f.setLayout(null);
		jf.setVisible(true);
	}	

	public int print(Graphics g,PageFormat pf,int page)
	{
		if(page>0)
			return NO_SUCH_PAGE;

		Graphics2D g2d=(Graphics2D)g;
		g2d.translate(pf.getImageableX(),pf.getImageableY());
		ta.setLineWrap(true);
		g.drawString(ta.getText(),50,100);
		return PAGE_EXISTS;
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==font)
		{
			jp.setBackground(Color.RED);
			b.add(jp);
			JInternalFrame a=new JInternalFrame("Font",true);
			a.setLocation(100,100);
			a.setSize(200,200);
			a.setVisible(true);
			jp.add(a);
		}

		if(e.getActionCommand()=="New")
		{
			new notepad1();
		}

		if(e.getActionCommand()=="Open...")
		{
			int x=jfc.showOpenDialog(null);
			if(x==JFileChooser.APPROVE_OPTION)
			{
				File f=jfc.getSelectedFile();
				path=f.getAbsolutePath();
				String name=jfc.getName(f);
				String s;
				ta.setText("");
				try
				{
					BufferedReader br=new BufferedReader(new FileReader(f));
					while((s=br.readLine())!=null)
					{	ta.append(s+"\n");
					}	
				}
				catch(Exception e1)
				{	e1.printStackTrace();
				}	
			}
			else if(x==JFileChooser.CANCEL_OPTION)
			{
			}
		}

		if(e.getActionCommand()=="Save")
		{
			String path=jfc.getSelectedFile().getAbsolutePath();
			File f=new File(path);
			try
			{
				BufferedWriter br=new BufferedWriter(new FileWriter(f));
				ta.write(br);
			}
			catch(IOException io)
			{	System.out.println(io);
			}	
		}

		if(e.getActionCommand()=="Save_as..")
		{
			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int x=jfc.showSaveDialog(this);
			if(x==JFileChooser.APPROVE_OPTION)
			{
				BufferedWriter fw=null;
				String path=jfc.getSelectedFile().getAbsolutePath();
				System.out.println(path);
				String file_name=jfc.getSelectedFile().getName();
				try
				{
					File f1=new File(path);
					if(f1==null)
						return;
					
					if(f1.exists())
					{
						int ans=JOptionPane.showConfirmDialog(this,"replace existing file??");
						if(ans==JOptionPane.NO_OPTION)
							return;	
					}

					fw=new BufferedWriter(new FileWriter(f1));
					ta.write(fw);
					fw.close();
				}
				catch(IOException i)
				{	i.printStackTrace();
				}	 
			}	
		}

		if(e.getActionCommand()=="Print...")
		{
			pj=PrinterJob.getPrinterJob();
			pj.setPrintable(this);
			if(pj.printDialog())
			{	try
				{	pj.print();
				}
				catch(PrinterException p)
				{	System.out.println("System is unable to print");
				}
			} 
		}

		if(e.getActionCommand()=="Exit")
		{
			System.exit(0);
		}	

		if(e.getActionCommand()=="Undo")
		{

		}

		if(e.getActionCommand()=="Cut"||e.getActionCommand()=="Delete")
		{
			String original=ta.getText();
			String selected_text=ta.getSelectedText();
			String final_text=original.replace(selected_text,"");
			ta.setText(final_text);
		}

		if(e.getActionCommand()=="Copy")
		{
			original1=ta.getText();
			selected_text1=ta.getSelectedText();
			final_text1=original1+selected_text1;
		}

		if(e.getActionCommand()=="Paste")
		{
			int x=ta.getCaretPosition();
			ta.setCaretPosition(x);
			ta.setText(selected_text1);
		}

		if(e.getActionCommand()=="View Help")
		{
		}
	}
	public static void main(String[] args) {
	 	new notepad1();
	}
}