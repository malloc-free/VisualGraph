/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.knights.visual.graph.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import nz.ac.aut.knights.visual.graph.api.GraphControl;
import nz.ac.aut.knights.visual.graph.api.GraphMessage;
import nz.ac.aut.knights.visual.graph.gui.GraphShape;
import static nz.ac.aut.knights.visual.graph.api.GraphModelCommand.*;

/**
 *
 * @author michael
 */
public class VertexSelectDialog extends JDialog{
    private final List<JCheckBox> checkBoxes;
    private final String retCommand;
    public VertexSelectDialog(JFrame parent, List<GraphShape> vertices,
            String retCommand, String title){
        super(parent);
        JPanel panel = new JPanel();
        setTitle(title);
        checkBoxes = new ArrayList<>();
        this.retCommand = retCommand;
        for(GraphShape v : vertices){
            JCheckBox temp = new JCheckBox(v.getName());
            temp.setActionCommand(v.getName());
            checkBoxes.add(temp);
            panel.add(temp);
        }
        
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                execute();
            }
        });
        
        panel.add(ok);
        add(panel);
        setLocationRelativeTo(parent);
    }
    
    private void execute(){
        final List<String> selected = new ArrayList<>();
        for(JCheckBox b : checkBoxes){
            if(b.isSelected()){
                selected.add(b.getActionCommand());
            }
        }
        
        final GraphMessage message = new GraphMessage(retCommand);
        
        message.add(new GraphMessage.EnumKeyValue(){{
            keyEnum = M_VERTEX_LIST; object = selected;
        }});
        
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                GraphControl.getControl(true).alertGraphListener(message);            }
        });
       
        dispose();
    }
    
    public static void display(final JFrame parent, final List<GraphShape> vertices,
            final String retCommand, final String title){
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                VertexSelectDialog dialog = new VertexSelectDialog(parent, vertices,
                        retCommand, title);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }
}
