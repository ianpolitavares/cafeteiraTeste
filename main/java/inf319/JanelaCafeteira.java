package inf319;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.LineBorder;

/**
 * Classe que implementa a interface gráfica que permite manipular o 
 * 'hardware' e observar o funcionamento da cafeteira.
 */

public class JanelaCafeteira extends JFrame 
        implements ChangeListener, ActionListener, ItemListener {
    
    private static final long serialVersionUID = -865097671714426223L;

    private Hardware hardware;

    private JLabel labelEbulidor;
    private JLabel labelValvula;
    private JLabel labelAquecedor;
    private JLabel labelIndicadora;
    private JProgressBar progressCafe;
    private JProgressBar progressAgua;
    private JCheckBox checkJarra;
    private JButton buttonFazer;
    private JSpinner waterSpinner;

    private boolean ready;
    
    public JanelaCafeteira(Hardware oHardware) {
        super();
        hardware = oHardware;
        ready = false;
    }

    public void preparaJanela() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                prepara();
            }
        });
    }
    
    protected void prepara() {
        // Prepara frame principal
        setResizable(false);
        setTitle("Cafeteira Harar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Instancia componentes da interface
        labelEbulidor = new JLabel();
        labelValvula = new JLabel();
        labelAquecedor = new JLabel();
        labelIndicadora = new JLabel();

        progressAgua = new JProgressBar(0, 50);
        progressAgua.setOrientation(SwingConstants.VERTICAL);
        progressAgua.setBorder(new LineBorder(Color.BLUE,2));
        SpinnerNumberModel waterModel = new SpinnerNumberModel(0,0,50,2);
        waterSpinner = new JSpinner(waterModel);
        waterSpinner.addChangeListener(this);
        JPanel controleAgua = new JPanel();
        controleAgua.setLayout(new FlowLayout(FlowLayout.LEFT));
        controleAgua.add(waterSpinner);
        controleAgua.add(new JLabel("Água"));
        
        progressCafe = new JProgressBar(0, 50);
        progressCafe.setOrientation(SwingConstants.VERTICAL);
        progressCafe.setBorder(new LineBorder(Color.BLUE,2));
        progressCafe.setForeground(new Color(99,58,8));
        JButton buttonTirarCafe = new JButton("-");
        buttonTirarCafe.setActionCommand("tirarCafe");
        buttonTirarCafe.addActionListener(this);
        JPanel controleCafe = new JPanel();
        controleCafe.setLayout(new FlowLayout(FlowLayout.LEFT));
        controleCafe.add(buttonTirarCafe);        
        controleCafe.add(new JLabel("Café"));

        checkJarra = new JCheckBox("Jarra", true);
        checkJarra.addItemListener(this);

        buttonFazer = new JButton("Liga");
        buttonFazer.setActionCommand("buttonFazer");
        buttonFazer.setBackground(Color.LIGHT_GRAY);
        buttonFazer.addActionListener(this);
        
        
        // Prepara painel dos estados do hardware
        JPanel p1 = new JPanel(new GridLayout(4,1));
        p1.add(labelEbulidor);
        p1.add(labelValvula);
        p1.add(labelAquecedor);
        p1.add(labelIndicadora);
        
        // Prepara o painel dos reservatorios
        JPanel p2 = new JPanel();
        LineBorder lb = new LineBorder(Color.DARK_GRAY,5);
        p2.setBorder(lb);
        
        GridLayout grid = new GridLayout(2,2);
        grid.setVgap(50);
        
        p2.setLayout(grid);
        p2.add(progressAgua);
        p2.add(controleAgua);
        p2.add(progressCafe);
        p2.add(controleCafe);


        
        // Prepara o painel principal
        JPanel p3 = new JPanel();
        p3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p3.setLayout(new GridLayout(1,2));
        p3.add(p1);
        p3.add(p2);
        p3.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel p4 = new JPanel();
        p4.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        p4.add(buttonFazer);
        p4.add(checkJarra);                
        p4.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().setLayout(
                new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(p3);
        getContentPane().add(p4);

        pack();
        setVisible(true);
        ready = true;
        atualizaEstado();
    }
    
    public void stateChanged(ChangeEvent e) {
    	JSpinner spinner = (JSpinner) e.getSource();
    	Integer spinnerValue = (Integer) spinner.getValue();
    	hardware.ajustaNivelDeAgua(spinnerValue.intValue());
    	progressAgua.setValue(hardware.pegaNivelDeAgua());
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("buttonFazer".equals(command)) {
            // buttonFazer.setBackground(Color.LIGHT_GRAY);
            hardware.pressionaBotao();
        } else if ("tirarCafe".equals(command)) {
        	if (hardware.leEstadoEbulidor().equals(EstadoHardware.ebulidorVazio) &&
        			hardware.leEstadoAquecedor().equals(EstadoHardware.placaVazia))
            hardware.ajustaNivelDeCafe(hardware.pegaNivelDeCafe() - 1);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (checkJarra.equals(e.getItemSelectable())) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                hardware.colocaJarra();
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                hardware.removeJarra();
            }
        }
    }
    
    public void atualizaEstado() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                atualiza();
            }
        });
    }
    
/* 
    public void atualizaEstado() {
    	     try {
    	    	 javax.swing.SwingUtilities.invokeAndWait(doAtualizaEstado);
    	     } catch (Exception e) {
    	    	 e.printStackTrace();
    	     }
    }
    
    public final Runnable doAtualizaEstado = new Runnable() {
       public void run() {
    	      atualiza();
       }
    };
*/          
    protected void atualiza() {
        if (ready) {
            if (hardware.leEstadoElementoEbulidor().equals(
                    EstadoHardware.ebulidorLigado)) {
                labelEbulidor.setText("Ebulidor");
                labelEbulidor.setForeground(Color.GREEN);
            } else {
                labelEbulidor.setText("Ebulidor");
                labelEbulidor.setForeground(Color.RED);
             //   buttonFazer.setBackground(Color.LIGHT_GRAY);
            }
            
            if (hardware.leEstadoValvulaPressao().equals(
                    EstadoHardware.valvulaAberta)) {
                labelValvula.setText("Válvula Aberta");
                labelValvula.setForeground(Color.GREEN);
            } else {
            	labelValvula.setText("Válvula Fechada");
                labelValvula.setForeground(Color.RED);
            }
            
            if (hardware.leEstadoElementoAquecedor().equals(
                    EstadoHardware.aquecedorLigado)) {
                labelAquecedor.setText("Aquecedor");
                labelAquecedor.setForeground(Color.GREEN);
            } else {
                labelAquecedor.setText("Aquecedor");
                labelAquecedor.setForeground(Color.RED);
            }
            
            if (hardware.leEstadoLuzIndicadora().equals(
                    EstadoHardware.indicadoraLigada)) {
                labelIndicadora.setText("CAFÉ!");
                labelIndicadora.setForeground(Color.GREEN);
            } else if (hardware.leEstadoLuzIndicadora().equals(
		   EstadoHardware.indicadoraDesligada)) {
                labelIndicadora.setText("CAFÉ.");
                labelIndicadora.setForeground(Color.RED);
            } else {
                labelIndicadora.setText("CAFÉ ");
                labelIndicadora.setForeground(Color.YELLOW);
            }
            
            progressAgua.setValue(hardware.pegaNivelDeAgua());
            waterSpinner.setValue(hardware.pegaNivelDeAgua());
            progressCafe.setValue(hardware.pegaNivelDeCafe());
        }
    }
}
