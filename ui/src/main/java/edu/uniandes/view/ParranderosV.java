package edu.uniandes.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.uniandes.UIConfig;
import edu.uniandes.business.ParranderosB;
import edu.uniandes.data.TipoBebida;
import edu.uniandes.exception.UserException;
import edu.uniandes.util.TextTable;
import edu.uniandes.view.input.DatePicker;
import edu.uniandes.view.input.TimePicker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "FeatureEnvy"})
public final class ParranderosV extends JFrame implements ActionListener {
    private final static Logger LOG = LogManager.getLogger(ParranderosV.class);
    private final static Supplier<JComboBox<String>> YES_NO = Suppliers.YES_NO;
    private final static Supplier<TimePicker> TIME = TimePicker::new;
    private final static Supplier<DatePicker> DATE = DatePicker::new;
    private final static Supplier<JComboBox<String>> PRESUPUESTO_BOX = Suppliers.PRESUPUESTO_BOX;
    private final static Supplier<JComboBox<String>> HORARIO_BOX = Suppliers.HORARIO_BOX;
    private final static Supplier<JTextField> TEXT_FIELD = Suppliers.TEXT_FIELD;
    private final static Supplier<JTextField> NUMERIC_FIELD = Suppliers.NUMERIC_FIELD;

    private final Console console = new Console();
    private final CommandLine commandLine = new CommandLine(this);
    private final ParranderosB business;

    public ParranderosV(ParranderosB business)
            throws HeadlessException {
        super("ParranderosB");
        setLayout(new BorderLayout());
        this.business = business;
    }

    private static String generateErrorMessage(Throwable e) {
        String clName, msg, title;
        if (e instanceof RuntimeException re) {
            try {
                clName = re.getCause().getClass().getSimpleName();
                msg = re.getCause().getMessage();
            } catch (NullPointerException npe) {
                clName = re.getClass().getSimpleName();
                msg = re.getMessage();
            }

        } else {
            clName = e.getClass().getSimpleName();
            msg = e.getMessage();
        }
        title = switch (clName) {
            case "IndexOutOfBoundsException" -> "No hay elementos";
            case "NullPointerException" -> "Inexistente";
            case "AssertionError" -> "User error";
            case "JDODataStoreException" -> "Error con SQL";
            default -> clName;
        };
        return "=-------------------" + title + "--------------------=\n"
               + msg.trim() + "\n" +
               "=-------------------" + title + "--------------------=\n";
    }

    public void configMenu(URL menu1URL)
            throws IOException {
        InputStream menu1 = menu1URL.openStream();
        Yaml menus = new Yaml();
        JMenuBar bar = new JMenuBar();

        JMenu util = new JMenu("Utility");
        menus.<Map<String, Map<String, String>>>load(menu1)
             .forEach((String k, Map<String, String> v) -> util.add(new JMenu(k) {{
                 v.forEach((String k1, String v1) -> add(new JMenuItem() {{
                     addActionListener(ParranderosV.this);
                     setText(k1);
                     setActionCommand(v1);
                 }}));
             }}));
        bar.add(util);
        setJMenuBar(bar);
    }

    public void configView(URL appURL)
            throws IOException {
        InputStream app = appURL.openStream();
        Yaml yaml = new Yaml();
        Map<String, Object> load = yaml.load(app);
        var body = new Object() {
            private final String title = String.valueOf(load.get("title"));
            private final int frameW = (Integer) load.get("frameW");
            private final int frameH = (Integer) load.get("frameH");
            private final String banner = "/" + load.get("banner");
        };
        setTitle(body.title);
        setSize(body.frameW, body.frameH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel jLabel = new JLabel(
                new ImageIcon(Objects.requireNonNull(getClass().getResource(body.banner)).getPath()));
        jLabel.setHorizontalAlignment(2);
        add(jLabel,
            BorderLayout.NORTH);
        add(console, BorderLayout.CENTER);
        add(commandLine, BorderLayout.SOUTH);
    }

    @Override public void actionPerformed(ActionEvent e) {
        String event = e.getActionCommand();
        try {ParranderosV.class.getDeclaredMethod(event).invoke(this);}
        catch (Exception ex) {throw new RuntimeException(event + "\n", ex);}
    }


    void createTipoBebida() {
        inputBlock(() -> {
            InputBuilder ib = new InputBuilder();
            ib.append("Nombre", TEXT_FIELD.get());
            int selected = JOptionPane.showConfirmDialog(null, ib.build(), "Adicionar Tipo de Bebida",
                                                         JOptionPane.OK_CANCEL_OPTION);
            if (selected == JOptionPane.CANCEL_OPTION) throw new UserException("CANCEL");
            return ib.getInput().getValues();
        }, business::createTipoBebida);
    }

    void retrieveOneTipoBebida() {
        inputBlock(() -> {
            InputBuilder ib = new InputBuilder();
            ib.append("Nombre", TEXT_FIELD.get());
            int selected = JOptionPane.showConfirmDialog(null, ib.build(), "Consultar Tipo de Bebida",
                                                         JOptionPane.OK_CANCEL_OPTION);
            if (selected == JOptionPane.CANCEL_OPTION) throw new UserException("CANCEL");
            return ib.getInput().getValues();
        }, input -> business.retrieveOne(TipoBebida.class, input));
    }

    void retrieveAllTipoBebida () {
        noInputBlock(() -> business.retrieveAll(TipoBebida.class));
    }

    void deleteTipoBebida () {
        String methodName = "deleteHotel";
        inputBlock(() -> {
            InputBuilder ib = new InputBuilder();
            ib.append("ID", NUMERIC_FIELD.get());
            int selected = JOptionPane.showConfirmDialog(null, ib.build(), "Consultar Tipo de Bebida",
                                                         JOptionPane.OK_CANCEL_OPTION);
            if (selected == JOptionPane.CANCEL_OPTION) throw new UserException("CANCEL");
            return ib.getInput().getValues();
        }, input -> business.delete(TipoBebida.class, input));
    }

    void cleanAlohandesLog () {
        try (Writer w = new BufferedWriter(new FileWriter("alohandes.log"))) {
            w.write("");
            console.print("ParranderosB.log limpiado exitosamente", null);
        } catch (IOException e) {
            console.print(e.getMessage(), false);
        }
    }

    void cleanDatanucleusLog () {
        try (Writer w = new BufferedWriter(new FileWriter("datanucleus.log"))) {
            w.write("");
            console.print("Datanucleus.log limpiado exitosamente", null);
        } catch (IOException e) {
            console.print(e.getMessage(), false);
        }
    }

    void about () {
        console.print("Equipo encargado::\n"
                      + TextTable.builder().appends(new Object[][]{
                              new Object[]{"Responsable", "Revisado por", "Fecha"},
                              new Object[]{"Pedro Lobato", "German Bravo", "2023-06-01"}
                      }),
                      null);
    }

    void clear () {
        console.print("Limpiando consola...", null);
        console.clear();
    }

    void exit () {
        console.print("Saliendo...", null);
        System.exit(0);
    }

    public void dark () {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("edu.uniandes.console.status.null", UIConfig.DARK_STATUS[0]);
            UIManager.put("edu.uniandes.console.status.true", UIConfig.DARK_STATUS[1]);
            UIManager.put("edu.uniandes.console.status.false", UIConfig.DARK_STATUS[2]);
            console.reset();
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    public void light () {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("edu.uniandes.console.status.null", UIConfig.LIGHT_STATUS[0]);
            UIManager.put("edu.uniandes.console.status.true", UIConfig.LIGHT_STATUS[1]);
            UIManager.put("edu.uniandes.console.status.false", UIConfig.LIGHT_STATUS[2]);
            console.reset();
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface interface Executable {
        Object[] call()
                throws Exception;
    }

    @FunctionalInterface interface Monad {
        String apply(Object[] strings)
                throws Exception;
    }

    private void inputBlock(Executable caller,
                            Monad lambda) {
        String result;
        boolean b;
        try {
            result = lambda.apply(caller.call());
            b = true;
        } catch (Exception | AssertionError e) {
            LOG.error(e.getMessage());
            result = generateErrorMessage(e);
            b = false;
        }
        console.print(result, b);
    }

    private void noInputBlock(Callable<String> lambda) {
        String result;
        boolean b;
        try {
            result = lambda.call();
            b = true;
        } catch (Exception | AssertionError e) {
            LOG.error(e.getMessage());
            result = generateErrorMessage(e);
            b = false;
        }
        console.print(result, b);
    }

    private void terminal (java.util.function.Function < ? super String, Object[]>input, Monad business, String
    methodName){
        inputBlock(() -> input.apply(methodName), business);
    }
}

class Suppliers {
    private final static int FIELD_SIZE = 20;
    final static Supplier<JComboBox<String>> YES_NO = () -> new JComboBox<>(new String[]{"Si", "No"});
    final static Supplier<JComboBox<String>> PRESUPUESTO_BOX = () -> new JComboBox<>(new String[]{"Alto", "Medio", "Bajo"});
    final static Supplier<JComboBox<String>> HORARIO_BOX = () -> new JComboBox<>(new String[]{"Diurno", "Nocturno", "Todos"});
    final static Supplier<JTextField> TEXT_FIELD = () -> new JTextField(FIELD_SIZE);
    final static Supplier<JTextField> NUMERIC_FIELD = () -> new JTextField(new javax.swing.text.PlainDocument() {
        @Override public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
                throws javax.swing.text.BadLocationException {
            if (str.matches("\\d+")) super.insertString(offs, str, a);
        }
    }, null, FIELD_SIZE);
}
