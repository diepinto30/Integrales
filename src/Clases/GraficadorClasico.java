package Clases;

import Ventanas.IntegralVentana;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import org.edisoncor.gui.util.WindowsUtil;
import org.nfunk.jep.*;  
import org.nfunk.jep.type.*; 

//public class GraficadorClasico extends JFrame
public class GraficadorClasico extends JDialog
{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JEP miEvaluador;  //  @jve:decl-index=0:
	boolean errorEnExpresion; //si hay error de sintaxis en la funci�n
	boolean errorEnNumero   ; //si hay error de sintaxis en la funci�n
        
	Font ft10 = new Font("Arial",Font.PLAIN,10);
	Font ft12 = new Font("Arial",Font.PLAIN,12);
	Font ft7  = new Font("Arial",Font.PLAIN,7);
	JTextField Tffun; 
	JLabel Mensaje; 
	JPanel SP; //Slider Panel
	JPanel ZG; //aqu� se va a poner la ZonaGrafica
	JPanel ControlPanel ; //panel para botones y campos de texto,etc 

	JPanel DisplayPanel1 = new JPanel(); //aqu� se va a poner ZG para obtener un buen borde
	JPanel DisplayPanel2 = new JPanel(); //aqu� se va a poner los Sliders y controlPanel
	JButton BtnAyuda,BtnGraficar; 
	JFrame fFrame; //ventana de ayuda 
        
	int Galto,Gancho; //dimesiones de la zona de graficaci�n 
	double xmin,xmax,imgx;
	int    x0,y0;           //origen
	int    escalaX,escalaY; 
	final static BasicStroke grosor1 = new BasicStroke(1.5f); //thickness
	final static float dash1[] = {5.0f};
	final static BasicStroke dashed = new BasicStroke(1.0f, 
			BasicStroke.CAP_BUTT, 
			BasicStroke.JOIN_MITER, 
			5.0f, dash1, 0.0f);



	public GraficadorClasico(String funcion) 
	{ 
		this.setTitle("Gráfica de Integral");
		this.setDefaultCloseOperation ( JFrame.DISPOSE_ON_CLOSE );		
		JPanel Contenedor = new JPanel();
		Contenedor.setSize(750, 600);               
		this.setSize(750,700);
		this.setContentPane(Contenedor);		
		Gancho = getSize().width-10;
		Galto = 70*getSize().height/100; 
		ZG = new ZonaGrafica(); //zona gr�fica
		SP = new SliderPanel(); //panel para Sliders de escala	
		ControlPanel = new JPanel();
		DisplayPanel1.setLayout(new BorderLayout());
		DisplayPanel1.add(ZG, BorderLayout.CENTER); 		

		ControlPanel.setLayout(new GridBagLayout()); // como hoja de Excel 
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		Tffun = new JTextField(funcion,8); 
		c.gridy = 1; // fila
		c.gridx = 0; // columna 
		ControlPanel.add(Tffun,c);
              

		//espacio en blanco
		JLabel esp1 = new JLabel(" "); 
		c.gridy = 1; // fila
		c.gridx = 1; // columna 
		ControlPanel.add(esp1,c); 

		Mensaje = new JLabel("",JLabel.LEFT); 
		c.gridwidth = 3;
		c.gridy = 4; // fila
		c.gridx = 0; // columna 
		ControlPanel.add(Mensaje,c); 
		Mensaje.setFont(ft12);
		Mensaje.setForeground(new Color(0,100,0));

		//fin del administrador de dise�o ControlPanel

		//Bordes 
		Border colorline = BorderFactory.createLineBorder(new Color(220,220,220)); 
		DisplayPanel1.setBorder(colorline); 
		TitledBorder rotulo; 
		rotulo = BorderFactory.createTitledBorder(" Escala");
		rotulo.setTitleFont(ft10);
		rotulo.setTitleColor(new Color(0,0,128));
		SP.setBorder(rotulo); 

		rotulo = BorderFactory.createTitledBorder(" CRV ");
		rotulo.setTitleFont(ft10);
		rotulo.setTitleColor(new Color(0,0,128));


		rotulo = BorderFactory.createTitledBorder(" Función ");
		rotulo.setTitleColor(new Color(0,0,128));
		rotulo.setTitleFont(ft10);
		ControlPanel.setBorder(rotulo); 
		//fin de Bordes 

		DisplayPanel1.setPreferredSize( new Dimension(Gancho,Galto)); 
		ControlPanel.setPreferredSize( new Dimension(60*Gancho/100,30*getSize().height/100));
		SP.setPreferredSize( new Dimension(30*Gancho/100,30*getSize().height/100));

		DisplayPanel2.setLayout(new BorderLayout(1,1)); 

		DisplayPanel2.add("Center", ControlPanel);
		DisplayPanel2.add("East", SP);

		Contenedor.setLayout(new BorderLayout(1,1)); //aplicar al applet
		Contenedor.add("Center", DisplayPanel1);
		Contenedor.add("South", DisplayPanel2); 

		miEvaluador = new JEP();                     
		miEvaluador.addStandardFunctions();  //agrega las funciones matematicas comunes
		miEvaluador.addStandardConstants();  
		miEvaluador.addComplex();  		
		miEvaluador.addFunction("sen", new org.nfunk.jep.function.Sine());//habilitar 'sen'			
		miEvaluador.addVariable("x", 0); 
		miEvaluador.setImplicitMul(true); //permite 2x en vez de 2*x
		escalaX=30;
		escalaY=30;
		x0=Gancho/2;
		y0=Galto/2;

		ManejadorDeEvento ManejadorDevt = new  ManejadorDeEvento();		 	
		Tffun.addActionListener(ManejadorDevt);	
                Tffun.setEditable(false);
//		BtnGraficar.addActionListener(ManejadorDevt);
//		fFrame = new AyudaJFrame (this);
//		BtnAyuda.addActionListener(ManejadorDevt);

                
                      WindowsUtil.makeWindowsOpacity(null,6f);
                      this.setLocationRelativeTo(IntegralVentana.Ventana);
                 

		
	}//


	private class ManejadorDeEvento implements ActionListener
	{
                @Override
		public void actionPerformed (ActionEvent evt)
		{ 
			Object source = evt.getSource ();
			// si se presiona el bot�n o se da 'enter' en alg�n campo de texto
			if ( source == BtnGraficar || source == Tffun)
			{
				ZG.repaint();
			}

			if(source == BtnAyuda)
			{      	           					   	  			
				fFrame.setVisible (true);		        	
			}//  

		}
	}//

        ///*************************************
	class ZonaGrafica extends JPanel  implements MouseListener, MouseMotionListener 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int     offsetX, offsetY;   
		boolean dragging; 

		ZonaGrafica() 
		{    
			setBackground(Color.white);             
			offsetX=x0; offsetY=y0;        
			addMouseListener(this);
			addMouseMotionListener(this);
		}


                @Override
		public void mousePressed(MouseEvent evt) 
		{  

			if (dragging)  
				return;
			int x = evt.getX();  // clic inicial
			int y = evt.getY();
			offsetX = x - x0;  
			offsetY = y - y0;
			dragging = true;                     
		}


                @Override
		public void mouseReleased(MouseEvent evt) 
		{                         
			dragging = false;        
			repaint();          
		}


                @Override
		public void mouseDragged(MouseEvent evt) 
		{               
			if (dragging == false)  
				return;

			int x = evt.getX();   // posici�n del mouse
			int y = evt.getY();
			x0 = x - offsetX;     // mover origen
			y0 = y - offsetY;    

			repaint();
		}

		//el resto hace nada 
                @Override
		public void mouseMoved(MouseEvent evt) {}           
                @Override
		public void mouseClicked(MouseEvent evt) { }
                @Override
		public void mouseEntered(MouseEvent evt) { }
                @Override
		public void mouseExited(MouseEvent evt) { }            


                @Override
		public void paintComponent(Graphics g) 
		{                   
			super.paintComponent(g);       // clear to background color  
			Gancho = getSize().width;
			Galto =  getSize().height;
//			x0=Gancho/2;
//			y0=Galto/2;
//			System.out.println (x0+ " " + y0 + " " + Gancho + " " + Galto);
			Graficar(g, x0, y0);         
			
			                    


		}

		void Graficar(Graphics ap, int xg, int yg)
		{
			int xi=0,yi=0,xi1=0,yi1=0,numPuntos=1;
			int cxmin,cxmax,cymin,cymax;
			double valxi=0.0, valxi1=0.0, valyi=0.0,valyi1=0.0; 
			

			//convertimos el objeto ap en un objeto Graphics2D para usar los m�todos Java2D
			Graphics2D g = (Graphics2D) ap;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 

			g.setFont(ft10); 
			g.setPaint(new Color(0,0,150)); 

			//eje Y
			g.draw(new Line2D.Double(xg, 10, xg, Galto-10)); 
			//eje X
			g.draw(new Line2D.Double(10, yg, Gancho-10, yg));

			xmin=-1.0*xg/escalaX;
			xmax=(1.0*(Gancho-xg)/escalaX);
			cxmin=(int)Math.round(xmin); //pantalla
			cxmax=(int)Math.round(xmax);
			cymin=(int)Math.round(1.0*(yg-Galto)/escalaY);
			cymax=(int)Math.round(1.0*yg/escalaY);

			numPuntos=Gancho; //num pixels

			g.setPaint(Color.gray); 
			g.setFont(ft7);

			//marcas en los ejes (ticks)
			if(escalaX>5)
			{
				for(int i=cxmin+1;i<cxmax;i++)
				{  g.draw(new Line2D.Double(xg+i*escalaX, yg-2, xg+i*escalaX , yg+2));
				if(i>0)
					g.drawString(""+i, xg+i*escalaX-2, yg+12);
				if(i<0)
					g.drawString(""+i, xg+i*escalaX-6, yg+12); 
				}
			}

			if(escalaY>5)
			{ 
				for(int i=cymin+1;i<cymax;i++)
				{  g.draw(new Line2D.Double(xg-2, yg-i*escalaY, xg+2 , yg-i*escalaY));  
				if(i>0)
					g.drawString(""+i, xg-12,yg-i*escalaY+3 );
				if(i<0)
					g.drawString(""+i, xg-14,yg-i*escalaY+3 );
				}
			}
			g.setPaint(new Color(50,100,0));

			g.setStroke(grosor1);

			miEvaluador.parseExpression(Tffun.getText());
			errorEnExpresion = miEvaluador.hasError(); //hay error?  

			if(!errorEnExpresion)
			{ 
				Mensaje.setText("--> Arrastre Con El Mouse Para Mover Los Ejes");
				Tffun.setForeground(Color.black); 

				for(int i=0;i<numPuntos-1;i++)
				{
					valxi =xmin +i*1.0/escalaX;
					valxi1=xmin+(i+1)*1.0/escalaX;
					miEvaluador.addVariable("x", valxi);
					valyi=miEvaluador.getValue();  
					miEvaluador.addVariable("x", valxi1);
					valyi1 =  miEvaluador.getValue();    
					xi =(int)Math.round(escalaX*(valxi));
					yi =(int)Math.round(escalaY*valyi); 
					xi1=(int)Math.round(escalaX*(valxi1));
					yi1=(int)Math.round(escalaY*valyi1); 

					//control de discontinuidades groseras y complejos
					Complex valC = miEvaluador.getComplexValue();
					double imgx = (double)Math.abs(valC.im()); 
					if(dist(valxi,valyi,valxi1,valyi1)< 1000 && imgx==0) 
					{ 
						g.draw(new Line2D.Double(xg+xi,yg-yi,xg+xi1,yg-yi1)); 
					} 
				}//fin del for 
			}else{Mensaje.setText("--> Se Encontraron Errores");
			Tffun.setForeground(Color.red);
			} 
		}//Graficar

		double dist(double xA,double yA, double xB,double yB)
		{
			return (xA - xB)*(xA - xB)+(yA - yB)*(yA - yB);
		}//

	} 
        
	class SliderPanel extends JPanel 
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JSlider xSlider,ySlider; // Manejo de escala   	

		SliderPanel()
		{   	   	 
			setLayout(new GridLayout(1,2));

			SliderListener auditor = new SliderListener();

			xSlider = new JSlider(JSlider.VERTICAL, 1, 200, 20);
			xSlider.addChangeListener(auditor); 
			add(xSlider);   	 	 

			ySlider = new JSlider(JSlider.VERTICAL, 1, 200, 20);
			ySlider.addChangeListener(auditor); 
			add(ySlider);

			//xSlider.setLabelTable(xSlider.createStandardLabels(20));                  
			// xSlider.setMajorTickSpacing(200);
			xSlider.setMinorTickSpacing(20);
			xSlider.setPaintTicks(true);
			xSlider.setPaintLabels(true);

			//ySlider.setMajorTickSpacing(200);
			ySlider.setMinorTickSpacing(20);
			ySlider.setPaintTicks(true);
			ySlider.setPaintLabels(true);                                

		}

		public void ajusteEscala()
		{ // se ejecuta si se 'oy�' alg�n cambio en alg�n Slider
			escalaX =(int) xSlider.getValue();    	                    
			escalaY =(int) ySlider.getValue();			            
			ZG.repaint(); 
		}//


		class SliderListener implements ChangeListener 
		{
                        @Override
			public void stateChanged(ChangeEvent e) 
			{  		        		         
				//JSlider source = (JSlider)e.getSource();
				// if (!source.getValueIsAdjusting()) 

				ajusteEscala();

			}    
		}          	    	    	 
	} //            	



} 



