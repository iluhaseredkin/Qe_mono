import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.awt.Container;
import java.util.Properties;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

public class Qe_mono
{
    double[][] N_mas;
    double[] result;
    JTextField TextJ_e;
    JTextField TextE_0;
    JTextField Textk1;
    JTextField Textk2;
    JTextField Textk3;
    JTextField TextDate;
    JTextField TextHeight;
    JFileChooser jfch;
    JFrame jf;
    String FileNameConc;
    JTextArea jta;
    final String homeDir;
    JProgressBar jpb;
    JButton jloadscript;
    JButton jsavescript;
    double k1;
    double k2;
    double k3;
    double E_0;
    double J_e;
    double G;
    double StepInFile;
    int NumOfPoints;
    int NumOfHeight;
    String ImageName;
    String out;
    double maxQe;

    public Qe_mono() {
        this.maxQe = 1000.0;
        this.N_mas = new double[800][4];
        this.result = new double[800];
        this.TextJ_e = new JTextField("1e8");
        this.TextE_0 = new JTextField("0.34");
        this.Textk1 = new JTextField("1");
        this.Textk2 = new JTextField("1.14");
        this.Textk3 = new JTextField("0.57");
        this.TextDate = new JTextField("");
        this.TextHeight = new JTextField("600");
        this.FileNameConc = null;
        final Properties prop = System.getProperties();
     //   this.homeDir = prop.getProperty("user.dir");
        this.homeDir = "/Users/iluhaseredkin/Documents/progz";
        this.jf = new JFrame("Calculate q_e");
        final Container con = this.jf.getContentPane();
        this.jf.setDefaultCloseOperation(3);
        final JPanel jp = new JPanel();
        (this.jpb = new JProgressBar(0)).setStringPainted(true);
        this.jpb.setMinimum(0);
        jp.setLayout(null);
        this.jf.setSize(458, 450);
        jp.setBackground(Color.GRAY.brighter());
        final JPanel jp2 = new JPanel();
        jp2.setBounds(0, 10, 450, 180);
        this.jta = new JTextArea("");
        final JScrollPane jsta = new JScrollPane(this.jta);
        jsta.setBounds(0, 200, 450, 180);
        final JLabel labelJ_e = new JLabel("\u041f\u043e\u0442\u043e\u043a \u044d\u043b\u0435\u043a\u0442\u0440\u043e\u043d\u043e\u0432, J_e (cm-2 c-1):");
        final JLabel labelE_0 = new JLabel("\u0425\u0430\u0440\u0430\u043a\u0442\u0435\u0440\u043d\u0430\u044f \u044d-\u044f \u043f\u043e\u0442\u043e\u043a\u0430, E_0 (keB):");
        final JLabel labelk1 = new JLabel("k1 (N2):");
        final JLabel labelk2 = new JLabel("k2 (O2):");
        final JLabel labelk3 = new JLabel("k3 (O):");
        final JLabel labelHeight = new JLabel("\u0412\u044b\u0441\u043e\u0442\u044b \u0438\u043d\u0442\u0435\u0433\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f (km):");
        final JButton jcalc = new JButton("Calculate");
        final JButton jopen = new JButton("Open file concentration");
        final JButton jreplot = new JButton("Replot");
        this.jsavescript = new JButton("Save new script");
        this.jloadscript = new JButton("Set default script");
        this.jsavescript.setEnabled(false);
        this.jloadscript.setEnabled(false);
        this.jloadscript.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                Qe_mono.this.LoadDefaultScript();
            }
        });
        jreplot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                Qe_mono.this.WriteScript(Qe_mono.this.jta.getText());
                Qe_mono.this.PlotGraph(Qe_mono.this.homeDir.concat("/gnu_script.gp"));
            }
        });
        jcalc.addActionListener(new Listen());
        (this.jfch = new JFileChooser()).setFileSelectionMode(0);
        jopen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if (Qe_mono.this.jfch.showDialog(Qe_mono.this.jf, "Open") == 0) {
                    Qe_mono.this.FileNameConc = Qe_mono.this.jfch.getSelectedFile().getAbsolutePath();
                }
            }
        });
        jp2.setLayout(new GridLayout(10, 2));
        jp2.add(labelJ_e);
        jp2.add(this.TextJ_e);
        jp2.add(labelE_0);
        jp2.add(this.TextE_0);
        jp2.add(labelk1);
        jp2.add(this.Textk1);
        jp2.add(labelk2);
        jp2.add(this.Textk2);
        jp2.add(labelk3);
        jp2.add(this.Textk3);
        jp2.add(labelHeight);
        jp2.add(this.TextHeight);
        jp2.add(jopen);
        jp2.add(jcalc);
        jp2.add(jreplot);
        jp2.add(this.jpb);
        jp2.add(this.jsavescript);
        jp2.add(this.jloadscript);
        jp2.setVisible(true);
        jp2.setBackground(Color.WHITE);
        jp.add(jp2);
        jp.add(jsta);
        con.add(jp);
        this.jf.setResizable(false);
        this.jf.setVisible(true);
        this.jf.setDefaultCloseOperation(3);
    }

    public void LoadDefaultScript() {
        this.jta.setText("");
        this.jta.setText(this.Script());
    }

    public void SaveNewScript() {
    }

    boolean readFile(final String str) {
        try {
            final RandomAccessFile f = new RandomAccessFile(str, "r");
            int i = 0;
            f.readLine();
            f.readLine();
            String s;
            while ((s = f.readLine()) != null) {
                this.N_mas[i][0] = Double.parseDouble(s.split(" ")[0]);
                this.N_mas[i][1] = Double.parseDouble(s.split(" ")[7]);
                this.N_mas[i][2] = Double.parseDouble(s.split(" ")[8]);
                this.N_mas[i][3] = Double.parseDouble(s.split(" ")[6]);
                ++i;
            }
            this.StepInFile = Math.abs(this.N_mas[0][0] - this.N_mas[1][0]);
            f.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return true;
    }

    boolean setParams(final double k1, final double k2, final double E_0, final double J_e, final double k3, final int Height) {
        this.NumOfHeight = (int)Math.round(Height / this.StepInFile);
        this.k1 = k1;
        this.k2 = k2;
        this.E_0 = E_0 * 1000.0;
        this.J_e = J_e;
        this.k3 = k3;
        return true;
    }

    double CalcIntegralOnHeigth(final int h) {
        final double E = this.E_0;
        final double R = this.k1 * this.N_mas[h][1] + this.k2 * this.N_mas[h][2] + this.k3 * this.N_mas[h][3];
        final double B = Math.pow(E, -5.8) + 5.0E9 * Math.pow(E, -8.8);
        final double q = 1.28E-13 * Math.pow(E, -0.854) * R * Math.exp(-1.46E-15 * B * Math.pow(R, 3.0));
        final double intf = this.J_e * q;
        return intf;
    }

    boolean WriteScript(final String scr) {
        try {
            final RandomAccessFile f_scr = new RandomAccessFile(this.homeDir.concat("/gnu_script.gp"), "rw");
            f_scr.write(scr.getBytes());
            f_scr.close();
            this.ImageName = this.out.concat(".png");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return true;
    }

    boolean WriteResult() {
        try {
            final RandomAccessFile f = new RandomAccessFile(this.out, "rw");
            for (int i = 0; i < this.NumOfHeight; ++i) {
                f.write(String.valueOf(this.result[i]).concat(" ").concat(String.valueOf(this.N_mas[i][0]).concat("\n")).getBytes());
            }
            f.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return true;
    }

    public String Script() {
        String s = "reset \nset terminal png";
        s = s.concat("\nset title 'q_e(H), J_e=".concat(String.valueOf(this.J_e)).concat(" cm^{-2} c^{-1}, E_0=").concat(String.valueOf(this.E_0 / 1000.0)).concat(" keB, {/Symbol g=").concat(" month_").concat(String.valueOf(this.FileNameConc.substring(this.FileNameConc.length() - 6, this.FileNameConc.length() - 4))).concat("'"));
        s = s.concat("\nset logscale x 10 \nset key off");
        s = s.concat("\nset xrange[10e-2:" + this.maxQe * 1.05 + "]");
        s = s.concat("\nset yrange[0:" + Integer.parseInt(this.TextHeight.getText()) + "]");
        s = s.concat("\nset xlabel 'lg(q_e ,cm^{-3} c^{-1})' offset 0,-1");
        s = s.concat("\nset ylabel 'H, km' offset 0,10");
        s = s.concat("\nset out '".concat(this.out).concat(".png'"));
        s = s.concat("\nplot '".concat(this.out).concat("' w l \n"));
        return s;
    }

    void PlotGraph(final String out) {
        try {
            Runtime.getRuntime().exec("/usr/local/Cellar/gnuplot/5.2.2_1/bin/gnuplot");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        new Qe_mono();
        System.out.println(Thread.currentThread());
    }

    class MyThread extends Thread
    {
        @Override
        public void run() {
            System.out.println(Thread.currentThread());
            Qe_mono.this.jpb.setMaximum(Qe_mono.this.NumOfHeight);
            for (int i = 0; i < Qe_mono.this.NumOfHeight; ++i) {
                Qe_mono.this.result[i] = Qe_mono.this.CalcIntegralOnHeigth(i);
                if (Qe_mono.this.result[i] > Qe_mono.this.maxQe) {
                    Qe_mono.this.maxQe = Qe_mono.this.result[i];
                }
                Qe_mono.this.jpb.setValue(i + 1);
                System.out.println("Height=" + Qe_mono.this.N_mas[i][0] + "   " + Qe_mono.this.result[i]);
            }
            System.out.println(Thread.currentThread());
            Qe_mono.this.out = Qe_mono.this.homeDir.concat("/q(h);J_e=").concat(String.valueOf(Qe_mono.this.J_e)).concat(";E_0=").concat(String.valueOf(Qe_mono.this.E_0)).concat(";k1(N2)=").concat(Qe_mono.this.Textk1.getText()).concat(";k2(O2)=").concat(Qe_mono.this.Textk2.getText()).concat(";k3(O)=").concat(Qe_mono.this.Textk3.getText()).concat(String.valueOf(Qe_mono.this.NumOfPoints));
            Qe_mono.this.WriteResult();
            Qe_mono.this.WriteScript(Qe_mono.this.Script());
            Qe_mono.this.jta.setText("");
            Qe_mono.this.jta.setText(Qe_mono.this.Script());
            Qe_mono.this.PlotGraph(Qe_mono.this.homeDir.concat("/gnu_script.gp"));
            Qe_mono.this.jsavescript.setEnabled(true);
            Qe_mono.this.jloadscript.setEnabled(true);
        }
    }

    class Listen implements ActionListener
    {
        @Override
        public void actionPerformed(final ActionEvent arg0) {
            final MyThread mt = new MyThread();
            if (Qe_mono.this.readFile(Qe_mono.this.FileNameConc) && Qe_mono.this.setParams(Double.parseDouble(Qe_mono.this.Textk1.getText()), Double.parseDouble(Qe_mono.this.Textk2.getText()), Double.parseDouble(Qe_mono.this.TextE_0.getText()), Double.parseDouble(Qe_mono.this.TextJ_e.getText()), Double.parseDouble(Qe_mono.this.Textk3.getText()), Integer.parseInt(Qe_mono.this.TextHeight.getText()))) {
                mt.start();
            }
        }
    }
}