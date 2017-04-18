package xbtplotter;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;
import java.sql.*;
import java.util.Comparator;
import java.util.Properties;
import java.util.List;
import java.util.Vector;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.StringTokenizer;
import java.lang.Math;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import binfileutils.XBTProfile;
import binfileutils.BinDecoder;
import javax.swing.JFileChooser;

@SuppressWarnings("deprecation")

public class XBTPlotter extends Frame
    implements ItemListener, ActionListener {
        private static SoopDataFTP ftpConn;
        private static String downloadedDir = System.getProperty("user.dir") +"/BinDownloads";
        private static ColorCanvas cc;
        private Thread refreshQuery = null;
        private String[] colorName;
        private Color[] color;
        private Choice choice;
        private static String[] MapVect;
        private static List<TransmittedProfile> Traces;
        private boolean refreshCurrentDate = false;
        public int count = 0;
        private int CurrentShip = 0;
        Button pp;
        static Dimension Screen;
        private static Toolkit DefKit;
        Panel pic;
        Choice CLong;

        static Calendar Start, End, NStart, NEnd, DateTime;
        private static DateFormat DateForm;
        private static Choice Ship;
        private static SimpleDateFormat formatter;
        private static String StartDate, EndDate;
        private static String PltStart, PltEnd;

        private static int clicked;
        int xLimMap, yLimMap, xSize, ySize;
        int CenterLon;
        float Ratio;
        private FontMetrics fm;
        static Color yell;

        static Choice BeginMon;
        static Choice BeginDay;
        static Choice BeginYear;
        static Choice Emon;
        static Choice Eday;
        static Choice Eyear;
        private static int[] DaysinMon;
        static int Selected;
        private static double MaxDepth = 0;
        private static double MaxTemp = -10;
        private static double MinTemp = 99;
        private int nobs = 0;
        Applet App;

        public static void InitXBTPlt()
        {
            ftpConn = new SoopDataFTP();
            if (!ftpConn.checkLogIn()) {
                JOptionPane.showMessageDialog(null, "Could not log in to FTP");
                System.exit(0);
            }
            if (!ftpConn.checkDLDirectory(downloadedDir)) {
                JOptionPane.showMessageDialog(null,
                        "Could not successfully create "
                                + "directory for downloaded files");
                System.exit(0);
            }

            Integer QueryDays = new Integer(8);
            Selected = 0;
            End = Calendar.getInstance();
            Start = Calendar.getInstance();
            Start.add(Calendar.DATE, -QueryDays.intValue());
            DateForm = DateFormat.getDateInstance(DateFormat.SHORT);
            formatter = new SimpleDateFormat("MMM-dd-yyyy");
            StartDate = formatter.format(Start.getTime());
            EndDate = formatter.format(End.getTime());
            Calendar Now = Calendar.getInstance();
            ArrayList<String> callsigns = new ArrayList<String>();

            SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                PltStart = fileDateFormat.format(formatter.parse(StartDate));
                PltEnd = fileDateFormat.format(formatter.parse(EndDate));
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                e.printStackTrace();
            }

            Ship = new Choice();
            Ship.setForeground(Color.black);
            Ship.setBackground(Color.white);
            Ship.setFont(new Font("SansSerif", Font.PLAIN, 12));

            BeginMon = new Choice();
            BeginMon.setForeground(Color.black);
            BeginMon.setBackground(Color.white);
            BeginDay = new Choice();
            BeginDay.setForeground(Color.black);
            BeginDay.setBackground(Color.white);
            BeginYear = new Choice();
            BeginYear.setForeground(Color.black);
            BeginYear.setBackground(Color.white);

            Emon = new Choice();
            Emon.setForeground(Color.black);
            Emon.setBackground(Color.white);
            Eday = new Choice();
            Eday.setForeground(Color.black);
            Eday.setBackground(Color.white);
            Eyear = new Choice();
            Eyear.setForeground(Color.black);
            Eyear.setBackground(Color.white);

            DaysinMon = new int[12];
            DaysinMon[0] = 31;
            DaysinMon[1] = 29;
            DaysinMon[2] = 31;
            DaysinMon[3] = 30;
            DaysinMon[4] = 31;
            DaysinMon[5] = 30;
            DaysinMon[6] = 31;
            DaysinMon[7] = 31;
            DaysinMon[8] = 30;
            DaysinMon[9] = 31;
            DaysinMon[10] = 30;
            DaysinMon[11] = 31;

            BeginMon.add("Jan");
            BeginMon.add("Feb");
            BeginMon.add("Mar");
            BeginMon.add("Apr");
            BeginMon.add("May");
            BeginMon.add("Jun");
            BeginMon.add("Jul");
            BeginMon.add("Aug");
            BeginMon.add("Sep");
            BeginMon.add("Oct");
            BeginMon.add("Nov");
            BeginMon.add("Dec");

            Emon.add("Jan");
            Emon.add("Feb");
            Emon.add("Mar");
            Emon.add("Apr");
            Emon.add("May");
            Emon.add("Jun");
            Emon.add("Jul");
            Emon.add("Aug");
            Emon.add("Sep");
            Emon.add("Oct");
            Emon.add("Nov");
            Emon.add("Dec");

            for (int i = 1; i < 32; i++) {
                BeginDay.add(new Integer(i).toString());
                Eday.add(new Integer(i).toString());
            }

            int ThisYear = Now.get(Calendar.YEAR);
            for (int i = 2012; i < ThisYear + 1; i++) {
                BeginYear.add(new Integer(i).toString());
                Eyear.add(new Integer(i).toString());
            }

            Eyear.select(new Integer(End.get(Calendar.YEAR)).toString());
            Eday.select(End.get(Calendar.DAY_OF_MONTH) - 1);
            Emon.select(End.get(Calendar.MONTH));

            BeginYear.select(new Integer(Start.get(Calendar.YEAR)).toString());
            BeginDay.select(Start.get(Calendar.DAY_OF_MONTH) - 1);
            BeginMon.select(Start.get(Calendar.MONTH));

            XBTPlotter.Ship.add("All Ships");
            callsigns = ftpConn.callSigns(downloadedDir);
            for (String cs : callsigns) {
                Ship.add(cs);
            }
            Traces = new Vector<TransmittedProfile>(24, 5);
        }

        private void QueryFTP() {
            ArrayList<String> callsigns = new ArrayList<String>();
            String callSignSelected = "";
            try {
                ArrayList<String> binFiles;
                super.setVisible(false);
                if (XBTPlotter.Ship.getSelectedIndex() != 0) {
                    callSignSelected = XBTPlotter.Ship.getSelectedItem();
                }

                binFiles = ftpConn
                            .getBinFileNames(PltStart, PltEnd,
                                    downloadedDir, callSignSelected);

                ftpConn.downloadFTPFiles(
                    checkRemainingBinFiles(binFiles), downloadedDir);

                for (String binFileName : binFiles) {
                    try {
                        if (!(new File(downloadedDir + "/" + binFileName)
                                .exists())) {
                            continue;
                        }

                        BinDecoder binDecoder =
                                new BinDecoder(
                                        downloadedDir + "/" + binFileName);
                        TransmittedProfile Prof =
                                new TransmittedProfile(binDecoder.getXBTProfile());
                        Prof.setFileNameAndRecoveryDate(binFileName);
                        Prof.setObservationDateTime();
                        Prof.setTemperaturePoints(new Vector<DTPair>(10, 10));

                        if (!Prof.temperatureObservations()) {
                            continue;
                        }
                        
                        Traces.add(Prof);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                        ex.printStackTrace();
                    }
                }

                if (Traces.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Profiles Not Found");
                } else {
                    Collections.sort(Traces);
                    nobs = Traces.size();
                    MaxDepth = Collections.max(Traces,
                            TransmittedProfile.Comparators.MAXDEPTH)
                            .getMaxDepth();
                    MaxTemp = Collections.max(Traces,
                            TransmittedProfile.Comparators.MAXTEMPERATURE)
                            .getMaxTemperature();
                    MinTemp = Collections.min(Traces,
                            TransmittedProfile.Comparators.MINTEMPERATURE)
                            .getMinTemperature();
                    Selected = Traces.size() - 1;
                    Iterator<TransmittedProfile> tracesIterator = Traces.iterator();
                    TransmittedProfile previousPR = null;
                    while (tracesIterator.hasNext()) {
                        TransmittedProfile currentPR = tracesIterator.next();
                        if (!(previousPR == null)) {
                            previousPR.distanceToNextPOS(currentPR);
                            currentPR.setPreviousPOS(previousPR.getNextPOS());
                        }
                        previousPR = currentPR;
                    }
                }
                super.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                e.printStackTrace();
            }
            super.setVisible(true);
            callsigns = ftpConn.callSigns(downloadedDir);
            Ship.removeAll();
            Ship.add("All Ships");
            for (String cs : callsigns) {
                Ship.add(cs);
            }
            if (!callSignSelected.isEmpty()) {
                XBTPlotter.Ship.select(callSignSelected);
            }
        }

        public static void main(String[] args) {

            XBTPlotter MyXBTplot = new XBTPlotter();
            MyXBTplot.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

            XBTPlotter.InitXBTPlt();

            clicked = 0;
            MyXBTplot.setBackground(Color.white);
            MyXBTplot.buildColorArray();
            MyXBTplot.BuildMapVector();
            MyXBTplot.setLayout(new BorderLayout());
            cc = MyXBTplot.new ColorCanvas();
            cc.setColor(Color.black);
            Panel pic = new Panel();
            pic.setBackground(Color.lightGray);
            pic.setLayout(new BorderLayout());
            
            Panel beginEndPanel = new Panel();
            beginEndPanel.setLayout(new GridLayout(1, 3));
            
            Label BD = new Label("Begin");
            //BD.setSize(100, 20);
            BD.setForeground(Color.black);
            BD.setFont(new Font("TimesRoman", Font.BOLD, 12));
            BD.setAlignment(Label.RIGHT);
            Panel pbd = new Panel();
            pbd.add(BD);
            pbd.add(BeginMon);
            pbd.add(BeginDay);
            pbd.add(BeginYear);
            beginEndPanel.add(pbd);

            Label ED = new Label("End");
            ED.setForeground(Color.black);
            ED.setFont(new Font("TimesRoman", Font.BOLD, 12));
            ED.setAlignment(Label.RIGHT);
            Panel ped = new Panel();
            ped.add(ED);
            ped.add(Emon);
            ped.add(Eday);
            ped.add(Eyear);
            beginEndPanel.add(ped);
            
            Panel CurrentDateBtnPanel = new Panel();
            JToggleButton currentDt = new JToggleButton("Current");
            currentDt.setBackground(Color.lightGray);
            currentDt.addActionListener(MyXBTplot);
            CurrentDateBtnPanel.add(currentDt);
            beginEndPanel.add(CurrentDateBtnPanel);

            Panel buttonsShipListPanel = new Panel();
            buttonsShipListPanel.setLayout(new GridLayout(1, 2));
            
            Label Ship = new Label("Ships");
            Ship.setAlignment(Label.LEFT);
            Ship.setForeground(Color.black);
            Ship.setFont(new Font("TimesRoman", Font.BOLD, 12));
            Panel ShipPanel = new Panel();
            ShipPanel.add(Ship);
            ShipPanel.add(XBTPlotter.Ship);
            buttonsShipListPanel.add(ShipPanel);

            Panel ButtonPanel = new Panel();
            Button query = new Button("Query");
            query.setBackground(Color.lightGray);
            query.addActionListener(MyXBTplot);
            ButtonPanel.add(query);
            buttonsShipListPanel.add(ButtonPanel);

            Panel uploadFilePanel = new Panel();
            uploadFilePanel.setLayout(new GridLayout(1, 4));
            
            Panel FileSelectionButtonPanel = new Panel();
            Button fileSelector = new Button("Select Files");
            fileSelector.setBackground(Color.lightGray);
            fileSelector.addActionListener(MyXBTplot);
            FileSelectionButtonPanel.add(fileSelector);
            uploadFilePanel.add(FileSelectionButtonPanel);
            
            pic.add(beginEndPanel, BorderLayout.WEST);
            pic.add(buttonsShipListPanel, BorderLayout.EAST);
            pic.add(uploadFilePanel, BorderLayout.SOUTH);

            
            //JFileChooser chooser = new JFileChooser();

            MyXBTplot.add(BorderLayout.CENTER, cc);
            MyXBTplot.add(BorderLayout.SOUTH, pic);

            Screen = DefKit.getScreenSize();
            MyXBTplot.setSize(1024, 768);

            MyXBTplot.pack();
            MyXBTplot.setSize(1024, 768);
            Dimension abc = MyXBTplot.getPreferredSize();
            Dimension bcd = MyXBTplot.getSize();
            Dimension cde = MyXBTplot.getMaximumSize();
            MyXBTplot.setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            String pp = e.getActionCommand();
            if (refreshQuery != null) {
                refreshQuery.interrupt();
            }
            if (pp.equals("Query")) {
                String bmon = XBTPlotter.BeginMon.getSelectedItem();
                String bday = XBTPlotter.BeginDay.getSelectedItem();
                String byr = XBTPlotter.BeginYear.getSelectedItem();
                String emon = XBTPlotter.Emon.getSelectedItem();
                String eday = XBTPlotter.Eday.getSelectedItem();
                String eyr = XBTPlotter.Eyear.getSelectedItem();
                StartDate = bmon + "-" + bday + "-" + byr;
                EndDate = emon + "-" + eday + "-" + eyr;
                CurrentShip = XBTPlotter.Ship.getSelectedIndex();

                SimpleDateFormat fileDateFormat =
                        new SimpleDateFormat("yyyyMMdd");
                try {
                    PltStart =
                            fileDateFormat.format(formatter.parse(StartDate));
                    PltEnd =
                            fileDateFormat.format(formatter.parse(EndDate));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                Traces.clear();
                QueryFTP();
                cc.repaint();
                
                refreshQuery = new Thread() {
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(1800000);
                                if (refreshCurrentDate) {
                                    Calendar systemDate =
                                            Calendar.getInstance();
                                    Eyear.select(new Integer(systemDate
                                            .get(Calendar.YEAR)).toString());
                                    Eday.select(systemDate
                                            .get(Calendar.DAY_OF_MONTH) - 1);
                                    Emon.select(systemDate
                                            .get(Calendar.MONTH));
                                    String emon = XBTPlotter.Emon
                                            .getSelectedItem();
                                    String eday = XBTPlotter.Eday
                                            .getSelectedItem();
                                    String eyr = XBTPlotter.Eyear
                                            .getSelectedItem();
                                    EndDate = emon + "-" + eday + "-" + eyr;
                                    SimpleDateFormat fileDateFormat =
                                            new SimpleDateFormat("yyyyMMdd");
                                    try {
                                        PltEnd = fileDateFormat.format(
                                                formatter.parse(EndDate));
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                Traces.clear();
                                QueryFTP();
                                cc.repaint();
                            }
                            catch(Exception e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                    }
                };
                refreshQuery.start();

            } else if (pp.equals(" JPEG ")) {
                do_print();
            } else if (pp.equals("Current")) {
                if (refreshCurrentDate) {
                    refreshCurrentDate = false;
                } else {
                    Calendar systemDate = Calendar.getInstance();
                    Eyear.select(new Integer(systemDate.get(Calendar.YEAR))
                            .toString());
                    Eday.select(systemDate.get(Calendar.DAY_OF_MONTH) - 1);
                    Emon.select(systemDate.get(Calendar.MONTH));
                    refreshCurrentDate = true;
                }
            } else if (pp.equals("Select Files")) {
                Traces.clear();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int returnValue = fileChooser.showOpenDialog(null);

                File[] selectedFile = fileChooser.getSelectedFiles();
                super.setVisible(false);
                for (File binFileName : selectedFile) {
                    try {
                        BinDecoder binDecoder =
                                new BinDecoder(binFileName.getAbsolutePath());
                        TransmittedProfile Prof =
                                new TransmittedProfile(binDecoder.getXBTProfile());
                        Prof.setFileName(binFileName.getName());
                        Prof.setObservationDateTime();
                        Prof.setRecoveryDateTime(
                                Prof.profileDateTimeFormat(0, 0, 0, 0, 0));
                        Prof.setTemperaturePoints(new Vector<DTPair>(10, 10));

                        if (!Prof.temperatureObservations()) {
                            continue;
                        }

                        Traces.add(Prof);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                        ex.printStackTrace();
                    }

                    if (Traces.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Profiles Not Found");
                    } else {
                        Collections.sort(Traces);
                        nobs = Traces.size();
                        MaxDepth = Collections.max(Traces,
                                TransmittedProfile.Comparators.MAXDEPTH)
                                .getMaxDepth();
                        MaxTemp = Collections.max(Traces,
                                TransmittedProfile.Comparators.MAXTEMPERATURE)
                                .getMaxTemperature();
                        MinTemp = Collections.min(Traces,
                                TransmittedProfile.Comparators.MINTEMPERATURE)
                                .getMinTemperature();
                        Selected = Traces.size() - 1;
                        Iterator<TransmittedProfile> tracesIterator = Traces.iterator();
                        TransmittedProfile previousPR = null;
                        while (tracesIterator.hasNext()) {
                            TransmittedProfile currentPR = tracesIterator.next();
                            if (!(previousPR == null)) {
                                previousPR.distanceToNextPOS(currentPR);
                                currentPR.setPreviousPOS(previousPR.getNextPOS());
                            }
                            previousPR = currentPR;
                        }
                    }
                }
                super.setVisible(true);
                cc.repaint();
            }
        }

        public void do_print() {
            Dimension d = this.getSize();
            Image OSI = createImage(d.width, d.height);
            Graphics g = OSI.getGraphics();
        }

        public void addComponent(String region, Component component) {
            Panel panel = new Panel();
            panel.add(component);
            add(region, panel);
        }

        private Choice colorChoice() {
            choice = new Choice();
            for (int i = 0; i < colorName.length; i++) {
                choice.addItem(colorName[i]);
            }
            choice.addItemListener(this);
            return (choice);
        }

        public void itemStateChanged(ItemEvent e) {
            int index = choice.getSelectedIndex();
            cc.setColor(color[index]);
        }

        private void buildColorArray() {
            colorName = new String[13];
            color = new Color[13];
            colorName[0] = "black";
            color[0] = Color.black;
            colorName[1] = "blue";
            color[1] = Color.blue;
            colorName[2] = "cyan";
            color[2] = Color.cyan;
            colorName[3] = "darkGray";
            color[3] = Color.darkGray;
            colorName[4] = "gray";
            color[4] = Color.gray;
            colorName[5] = "green";
            color[5] = Color.green;
            colorName[6] = "lightGray";
            color[6] = Color.lightGray;
            colorName[7] = "magenta";
            color[7] = Color.magenta;
            colorName[8] = "orange";
            color[8] = Color.orange;
            colorName[9] = "pink";
            color[9] = Color.pink;
            colorName[10] = "red";
            color[10] = Color.red;
            colorName[11] = "white";
            color[11] = Color.white;
            colorName[12] = "yellow";
            color[12] = Color.yellow;
        }

        private void BuildMapVector() {
            MapVect = new String[119];

            MapVect[0] = "328 -12.3  136.95859625356525542563956415442544452434444404640464052404940484049"
                    + "47446046594757476047484359446047604560496047544256425442574659485345514359475744"
                    + "57435943584857506046595060536052605558545557585459536056595157505360506057605356"
                    + "45604655545854564658496347594555455347474354445352614246435660525259465049454249"
                    + "44454251585756596053476040504359435447585059455850625060556052604960556054605753"
                    + "51565060496152615556495457595060486043604450464340494352395638503959405240584548"
                    + "56433954405344464152444554504340444146435142454347415040484243444044415042445843"
                    + "48474152514342454245494245494941534457455040404545454450474448414941455046545242"
                    + "5545514051434943554645475345";

            MapVect[1] = "054  18.6  120.75143504263476054585763465145474554386050455945555849426054545947"
                    + "5355385749444056435641505245404940504447";

            MapVect[2] = "054   9.8  125.55941604759515653435353565644565546584656445047474458545151554653"
                    + "5957394948414650504154474645464849424352";

            MapVect[3] = "138  -1.3  130.74547463950415440625060415345434144424443544154395142534254395441"
                    + "54395542554356525342534054465558574559435544554455455245545446545058486044603759"
                    + "46604960576057475458475450584954416049585354516140444844405443534459485949584559"
                    + "436156545055445044594638504346505057515945554759";

            MapVect[4] = "022  -5.5  148.35039494046434146534262545559526148594457";

            MapVect[5] = "014  -2.8  150.8553658405853405546544362";

            MapVect[6] = "010  -5.5  154.85544584645584252";

            MapVect[7] = "010 -10.0  160.04454504456445058";

            MapVect[8] = "014 -20.3  164.1544159405741476144583959";

            MapVect[9] = "026 -40.8  144.8524152424842494260485953586050604355425444524351";

            MapVect[10] = "010 -17.9  177.04341574152584860";

            MapVect[11] = "062 -34.5  172.75638584460465246445052465948534251444443514661515453526055505956"
                    + "55555557475744444451475745574742404644504356435544564154";

            MapVect[12] = "048 -40.7  172.55545484158485755575957474957536159525954566249614760475942444040"
                    + "4840434045404348484543474745";

            MapVect[13] = "022     0  128.04053404560494743575354474957535059504153";

            MapVect[14] = "014  -3.2  128.0473951375846475850604760";

            MapVect[15] = "010  -3.8  126.64356494056485256";

            MapVect[16] = "010  -6.9  134.14151454557485756";

            MapVect[17] = "018 -10.3  123.740474438484049415660545757585259";

            MapVect[18] = "012  -9.7  119.047405740525547554760";

            MapVect[19] = "018  -8.7  120.046445240504245445752536150584759";

            MapVect[20] = "016  -9.0  115.94148534146415638546150605061";

            MapVect[21] = "050  -8.3  115.65363476251574859485947604859535647564962465047594344504052405645"
                    + "51405043434554405443574450385636";

            MapVect[22] = "102     0  120.15848554550464545504348405451505355575455495456445745565354425547"
                    + "57525354465444505353485443504556414954576050574958505060414739525156425142454149"
                    + "46464349404747435340504051404339615452604959506151645352";

            MapVect[23] = "014   8.5  117.3414241414144605260595762";

            MapVect[24] = "090     0  109.14051404552405244434843454939414344464640504640444041554355445740"
                    + "53595647525857536245535156455544515851545752605656575546605757565356435349594756"
                    + "54634351535949573751464942564653";

            MapVect[25] = "076     0  103.55653544059454643514364465155595160515851506141624160405940564055"
                    + "40574057425145624253425745593857504353444943544160425543564358435540554351455545"
                    + "5551";

            MapVect[26] = "094  10.4  106.95858565854544253434744594760395443565160425051606450605660535848"
                    + "48446148594655464943574260446046605160445948515643584558405742563452395542564659"
                    + "4151414739493850405139564152405143575760";

            MapVect[27] = "010  13.5   92.76049585341514147";

            MapVect[28] = "078  16.3   96.05558486139483851435444554847465941574153405553585755515952604551"
                    + "54545558554858585361575657575757565956525158545250565654594960505850595461496058"
                    + "56595856";

            MapVect[29] = "020  10.0   80.0534558456244584956575260385436493747";

            MapVect[30] = "238   8.2   77.44260385439534157395439544254425440544052405240524047435343516053"
                    + "54604960415845544941464650604261475742534954455048575259526051605160496048604963"
                    + "48614452435350595663486045594456485643544255435649564553575556555845574659435745"
                    + "57505345424555465649465363444943514041404340444561495944554253405746544460576057"
                    + "53566149526058525361575550575358545857555459536052575759515856625060545852594956"
                    + "44534152405439484156435738564157436041534249405544564757435141544257425444585257"
                    + "42593856";

            MapVect[31] = "130  30.0   32.86049604260456046604560436148574062466050614756425944614757485640"
                    + "54425744594059435558504359445343463752434543504248445146464046445245595159536052"
                    + "60576055605360586058595858605660576060586060605956576156605558535752584260516049"
                    + "60475743615261506150594757525757";

            MapVect[32] = "070 -12.0   49.35844624860476052465359515852615260546253595360546053595458555158"
                    + "535647574558415240544050434644454245395437533944474046404440364646413846";

            MapVect[33] = "384 -16.3   40.05860526056575858575956555652584659465750605157535361545956536049"
                    + "60526059606060555657585858605560516054604859506151605360556045604456405446464154"
                    + "42564054415343574157385340504155395043494258415541564155405539503747404841464246"
                    + "47444048405144554253424638544156395340584059405838564345394640473948415644575160"
                    + "51605158435640554661506051605460536054605460486048605160526054615255466046574257"
                    + "43604560415840534058406140535060465440504051445543424048414738504255434742584047"
                    + "41444045414339454444434443444741454347434546444644544249454542464646474145434144"
                    + "45484645544453434839524147404543484246394841493750405241474151425041494047415244"
                    + "50435552545356455651545454555645544352415140524054405844534052405442534243424053"
                    + "434047405337545352395140544749405342533852414743484050405443";

            MapVect[34] = "026  31.0   32.7504349404144404740444347415239465258475657605161";

            MapVect[35] = "014  35.0   32.6554849434643454652605360";

            MapVect[36] = "058  36.0   32.94459486057535157455948594356445347584542445942484546486050605260"
                    + "564551584559594657455442564648565451485957474956";

            MapVect[37] = "016  35.6   24.05352533850424942485749624857";

            MapVect[38] = "094  37.3   23.05946485454544353535246534154484448574353415843563947445844614560"
                    + "46604259465048555455425452625449575154445441564956445442504153525442544056415353"
                    + "4654475957545445555052555550545649534350";

            MapVect[39] = "022  38.1   15.85162496151595548534053425344464744514546";

            MapVect[40] = "022  38.7   16.04846425347554557455747524960445843614755";

            MapVect[41] = "012  42.9    9.654585750544542454352";

            MapVect[42] = "020  41.2    9.3554357505954525647554249445347494741";

            MapVect[43] = "010  40.0    3.15346545445554845";

            MapVect[44] = "052  43.0   10.64053485848565657536054565057475948605659594854555458536059595554"
                    + "574556555654535955545160516053605354";

            MapVect[45] = "078  36.1   -5.44758445248565257506240484551505441514245424837504255474147475139"
                    + "50415139504051395141474543484349405041604555476050574256484049405245504140545043"
                    + "53495041";

            MapVect[46] = "048  51.3   -9.64255474543454759474548554847503946444957454150415041554554505356"
                    + "5749565155535159535753595157";

            MapVect[47] = "138  50.0   -5.84438474449414645515747595158475248384250515645464938434846584545"
                    + "52595160465046464653555150564450454752555054464847494557534144514844444850404940"
                    + "53505460534750415040555055555453535451435342584754425744554853514940564454525357"
                    + "535351415455526049585160525549615353505952555058";

            MapVect[48] = "240  49.3    -.14944475448414842445047404742463848574344494356514542485545465040"
                    + "47455142474241524353495340494545494147484943544952545647504653515257544754455242"
                    + "48504856455149385950545548604661574752395352504347425043534351454839484146404838"
                    + "56464940444850384648435243514244494258384643405248585359525545514840484247514742"
                    + "49375040514050464746484350404754515847565060516351605259515849594750476139474055"
                    + "44384440444044424557496250615657555453465659536155615659565157525548523954465656"
                    + "555553606051";

            MapVect[49] = "010  57.9   19.45654545543504741";

            MapVect[50] = "100  57.5   16.96055555450605756506441514848445840573955465556535156555954604959"
                    + "43624147405441524446464046394841453842404540414042434541434045474760473953404741"
                    + "4857474347384936474049404940474048415038494251375241";

            MapVect[51] = "102  76.5   17.04566466344524939484050404962485853505160506045624654455448405040"
                    + "50405150516052404743553943484942523854405250524051405143525150455246525351375250"
                    + "51405146535952634851516645434757475748415165555756605960";

            MapVect[52] = "058  79.4   20.94862504049404950516150595058456050394840524050405247464350465249"
                    + "493851405136535052615250516052605060475350615057";

            MapVect[53] = "128  70.6   29.65141524253615250514049395241535051395141524042405340524162415440"
                    + "56465657535951634860496149604860495752565339554259505450504055455138474647584657"
                    + "47455241524052405043415946424740464252395244424548574148445752385039563951615357"
                    + "5440534750414841435048414840";

            MapVect[54] = "704  59.9  -43.94860455748575060486145574358435643594556405245574757454947454758"
                    + "45484446474250414545464746554762485651525246524252505260516046604550484046564944"
                    + "51405140514045574758465552605158506047524743474646474756445445564753465647624753"
                    + "48464859496048614960496052595060515749575252496249604759484150414961495949624848"
                    + "49404939503950414746516250605156486049554958486048544747494149414939474050395040"
                    + "48404743495349424857506249614852484248404841484249395141494448544944485648405040"
                    + "51455245514048574855495849405040494050404942523848495039514752445141504148594955"
                    + "47545040514051465139514251435144514549424860486049584756504051375239476550395039"
                    + "50414962506150595060515949584944493949435041494050405040504050404940494048405040"
                    + "50405040504050405040504050404939514151405140516050605060506051605040504050405040"
                    + "49405040534351554936523651405141515951585156506050605061505950605060506050595263"
                    + "51585162525250424840494049385039503950415040504653535164504050405040474949415040"
                    + "52445354515351615258525749354939493947404945514249354940503951405040524052465260"
                    + "52584964534551625063516150405139536150595061514250405255525551485258524752595848"
                    + "53565341534354515064496252575549503951435447534451405346506549615349534253515160"
                    + "51615237545351625158505853435144544953534960486154395442534459505062486144554958"
                    + "54485450536150395039514150405460536054605360526051605260526050605057586155605560"
                    + "536254604960516056605750515855465557535957505753565457535259";

            MapVect[55] = "108  63.3  -18.65058495847544960496149424455506148535042484050554742495851604958"
                    + "46444745484850435244534553515347464650464650534247505042494352454841464951445247"
                    + "49425352514652505241544953515254495354505257495253585256515752585155";

            MapVect[56] = "058  71.6 -120.55262525951564656466350604444464544464744445849384937503952435241"
                    + "494050435241544052435268545953605354505654565349";

            MapVect[57] = "154  69.3 -116.04760465449464940494050405140504148494960496051644961506246584845"
                    + "49404839516150644856444650444856464146354835514654475357493847465139524154554636"
                    + "48545238524355495444484444554353484850404748493452435352545754485548544455525441"
                    + "52355337555050655444566249604960485947575558516252605059516150595057435648625060";

            MapVect[58] = "144  68.8 -117.04940514055405464524150405140493848375341534054434156463951654550"
                    + "50394840524254455240514052405040494052404347484048594859463945455341554054445266"
                    + "54405847424650394445495944454759486046564644435045404452415543464937504052405040"
                    + "514356615657496051595344546053425341565254395757513756445744";

            MapVect[59] = "046  71.7  -96.55262535749584559466447604646543948414553496047474839494049405250"
                    + "524055605244534353475651";

            MapVect[60] = "012  74.6  -95.146664430543756515066";

            MapVect[61] = "042  75.0  -98.05160506049584442506151595158446244575042533955384454514147435140"
                    + "4942524257495753";

            MapVect[62] = "082  75.0 -106.05160506049605260526052605161476348404940504147455162516248565360"
                    + "49604960495645444541483950405250504052415539504050404758484546624842494055435448"
                    + "4840514054455557";

            MapVect[63] = "040  76.0 -119.05360486050604860473847364742474450405040484552395360524353555058"
                    + "535545545761";

            MapVect[64] = "022  77.8 -113.24838504050375363504053535157506149584653";

            MapVect[65] = "020  78.2 -109.6506150594958515948434939484053395252";

            MapVect[66] = "042  78.5 -105.05034465952584849505746494940514055404844533552455441535250634656"
                    + "4962495751594860";

            MapVect[67] = "028  78.0  -97.94940496144545040513952415243515752445257515850584758";

            MapVect[68] = "070  78.1  -88.84958525449604960485849404962475847405039485351604960515648574557"
                    + "404049404937506349555036514054395340504051375743514053405261535952595559";

            MapVect[69] = "254  76.5  -89.84641493847464761484350395440434850605061444350385039435246694548"
                    + "50365040504050404940484049404940506051615159516051605060506049605060496048604460"
                    + "48455166495948405037473951385037486150374854503851395040475049404940494051405040"
                    + "47405040514052404748503750405040514150385041514252595164503849424940494051405340"
                    + "50405240526053605160506051605060525050404935525551605360526052605259535954625060"
                    + "52605060516050605140503562625263506354455261506152405251503953495258516051654745"
                    + "5059515950595062515349615060506050604858";

            MapVect[70] = "090  76.6  -91.05340514351575060524053405240504051424738494050405040514051415145"
                    + "54585141545850575160496051605060506050605060486052604860496043544946426051605060"
                    + "48604760463951415139544349385040";

            MapVect[71] = "188  72.1  -89.93838473949385135545447374838503551375040504051395743506253365534"
                    + "54425039523956395237534056405262534452495058564754395241494056405340514054425558"
                    + "56605757446344604560585856415340574166504560476048604858564254405840496049604860"
                    + "45604860436046604560515950605358486345504540514050404836435847424337415646614660"
                    + "52654345476044605459526150594964496149614964486249634659474048654151";

            MapVect[72] = "014  68.3  -76.4523656515459506044524442";

            MapVect[73] = "040  65.8  -85.55440544053415241564353425258496045605560545851564452505849574640"
                    + "465342464545";

            MapVect[74] = "012  68.7   48.946534642534154515163";

            MapVect[75] = "020  80.0   47.9465848354938484253405162525151625262";

            MapVect[76] = "026  80.6   57.1506249594958473848414840504052385065524454565159";

            MapVect[77] = "020  80.3   61.6505950594853474550404939534750605348";

            MapVect[78] = "018  81.0   65.550625357485847484940494451445347";

            MapVect[79] = "100  70.4   57.34959495949604959464949594960434949414652454448384655484747384144"
                    + "49404840464048404940494051404940484049404850494449395040534454585160526051605260"
                    + "5160526152595360536255585661546057485956554052425355";

            MapVect[80] = "272  68.0   50.04740494048404740514355575360555047404643484149405043484149385552"
                    + "50434751493844564558476248435241534251415041514153415338524252405540444247594753"
                    + "48604550454745544847494243404144494646504940534452635151503951415242595753535542"
                    + "60516051554058565660536050605060534050405440434045404241455547404938485951605058"
                    + "40524445436244454939454545604946534255466053513951395142485349604758475248425239"
                    + "46604742524051415339534450334949495646565061435144555039503950414938494050424944"
                    + "4754495747454743474047404940503949414941475050395040504051414940494248485040";

            MapVect[81] = "064  78.8   97.94959496050614659496047414846526151595060465350404948495648414748"
                    + "484149384942514253415143526053465143513852435653534751575064";

            MapVect[82] = "030  77.9   99.445444544464049425240524152425449515650605160506351585261";

            MapVect[83] = "198  76.4  100.04338473948404941524051405160525950355140514552555256504050434849"
                    + "50405140494051405342514453445654545953615260535853625256526051595453484550384939"
                    + "50404642494850594952484451405340494146544860484550415246525251525446504349405041"
                    + "51405040524052555241504251395040514146504654494150415045534949405248504151415242"
                    + "52405452535449585156554055405241524642414642544752414742504050405040504052434548"
                    + "47564846";

            MapVect[84] = "028  74.6  139.24960465848544647474648415045524154405354535351575354";

            MapVect[85] = "022  75.0  144.04960485645434762444858375143524154545256";

            MapVect[86] = "028  74.7  150.04961505648614653485452435041514251455042514252505260";

            MapVect[87] = "022  73.2  143.55059496249565059484148484939524153445251";

            MapVect[88] = "088  72.4  139.75041484448545042514250415140514051415141503951405242545353425039"
                    + "53435248504049394941503950405140524254445556524052424840493951395241503951444440"
                    + "4944524254534860554852405243";

            MapVect[89] = "848 -78.2 -176.65161502850275027492651304929482848294839464248304937503149324738"
                    + "47395032493148584730483850334939473549354639483950324930493249335032483848394549"
                    + "53325135514051395031503451364840524251395039514054505138435050375845483450365642"
                    + "50434355474648564753503549404861506646544646475549384848493951365038524051455547"
                    + "53384839494149374934534154585132503451365037544049404544523548414757484052345139"
                    + "51375138455047594766475944575334464950364655516247434651486446505040504050404753"
                    + "47384839534055454940474343444455496344504740534448434841465048434441455050394741"
                    + "47454743474149424742474150395555516353575552544856535454555355565652554955505549"
                    + "55515551545053515643545055485450555054485352545655575156544053385339543453325238"
                    + "53315237523754325233514049354739513053455021473947414836493948374738484348394838"
                    + "47454748473647414650484048394737474446474743504049354742474350374837464646374843"
                    + "46435038503649365037493848384039564353405032513550404839503651395139483950394741"
                    + "49424741554051405137503951405235514050394937494048364838473752405137454545405342"
                    + "52395440504154484840484244504337484149394839493847415343494048404860493551424250"
                    + "47414842494051405240524053425552503753395345493850375139503451375238503350435943"
                    + "46354842573853364937524546414642464348384642464442564647524253425041524147424840"
                    + "49425538534951404739493750405139454542444741514253435440484047415341484049405242"
                    + "54465240514052405540453646444742494352375243544054385243504150404733425052405242"
                    + "47455040504051415542544251374841374453425144503550415038514152365239524051405140"
                    + "52414943504056405334513953505038514249414938503850385838523851365338544054465132"
                    + "54404840493154415238553756475657556156585360555049645360565960505947584347244650"
                    + "4741513249305237532851255304";

            MapVect[90] = "228  68.2  -90.04140544061505542454345535242434650424556445050425140513854465143"
                    + "56495061574553425760536052595061475451625242586252605240596052605060545350595544"
                    + "53575057576060606057605054435040594658474941494053395340564152405440514150405236"
                    + "60526147604159394440444442564250435748404438454342474253445946594344444444524655"
                    + "43454754445047445240514052434741534255405640513659515947544256414740454241444545"
                    + "57425840614059425755553955415437533855614941554253425850564955605060";

            MapVect[91] = "062  50.9  -56.94537595459564744514453545142514051445852494359574642584857524862"
                    + "41495861515843424961516050594961465046434956444139434245";

            MapVect[92] = "014  49.8  -64.3513753405444516046604559";

            MapVect[93] = "310  51.3  -57.75660576350605060506050504960516050605460535452565660555656544440"
                    + "45404640484050405441585950605139595159405440514050434550413857505446545752575459"
                    + "53605260576050604350444043445360535953575357516056565158585658505443516352575556"
                    + "52635851585646586046575557534149465659505445545158485756555757605560596055576054"
                    + "60496046564760475849585058545057455047554255385743474356435650575458455647575363"
                    + "48595257535457425152465653545059435753534860526054605460546062546048605560516049"
                    + "60505546555160426043534155404840474052404551444237484742473950425142585459546652"
                    + "455463505853575451415040504049395440544360506053605159526144584054404440";

            MapVect[94] = "056  21.9  -84.64339463950395141544153415540553956395637536049605164424549604454"
                    + "48574958465550574858474548615657505554534756";

            MapVect[95] = "042  19.6  -73.44943514248455241524552405943535348605170575044535058506046575042"
                    + "5044484444534657";

            MapVect[96] = "300   9.4  -80.04840544052475643444345454548404448404740464049416055484041445640"
                    + "55425648494051405340454050405642563958475242554059445740544451394940533955405543"
                    + "54455444584960475444585258575960414250415040554252385340544056445746464056405040"
                    + "49405541574056425542494159446148605159526057585753555450565657555557585260506049"
                    + "60525750565558495955605660535359575250605060546053605860555854565952574960515652"
                    + "56595853595658575456575656555654575853574961465949584855574653425851534557505655"
                    + "54525457536052605160486056505851544955565161456055535849534348455650535656585749"
                    + "5654526053585654564454455042565055525358565756535553";

            MapVect[97] = "028 -52.1  -60.34956484448524651504250405042544751535358525846505357";

            MapVect[98] = "204 -50.1  -68.05360565156495643584955485540554053405263525950615260475743624359"
                    + "43604054425343544253424941494049465150625244423943434348445141494045476057505853"
                    + "50554448435043483848404641534052484340474044404840474049405142514447435244474047"
                    + "38483948405040514045405040514051405243584260455946604658415942544154415642544155"
                    + "40544054405644544561415041514442444443603850404340434443394550434044415240514049"
                    + "43564054445549565657";

            MapVect[99] = "252   8.4  -80.05947525945544654485851564558464845564760495641504455445645574557"
                    + "48594560496044604457455746574959546149584861445949584559475845554860465744584658"
                    + "42544148445342524456445643584353485945534649465645564355455643554356405446514758"
                    + "47595649574857485643554456475646564655455648604455495146584355554553445745574756"
                    + "44484550455746545056475543564741465044564755475644524255425543544454475745584758"
                    + "51564252425941563955455545554251425542464254445240464048405041504254425343564658"
                    + "455948604452444544534260435742554358";

            MapVect[100] = "010  18.9 -155.93949573956584854";

            MapVect[101] = "016  54.0 -131.75064594456455542425543534547";

            MapVect[102] = "060  54.9 -131.04565425944604261445946654461456348664963486348614758475651625550"
                    + "5557536153605060434741425035466354605657565857595142";

            MapVect[103] = "022  58.4 -152.75456525553595846494345444843465446454955";

            MapVect[104] = "072  58.9 -153.35758546054605455545953595453536152595160546054635160464547384741"
                    + "4644514044444742494347494543454741445363475255544464556442534946425756574861";

            MapVect[105] = "014  60.0 -165.6475351575058524853404744";

            MapVect[106] = "216  59.8 -164.14557475647464558465244444544464253454538504047454355504547535159"
                    + "54574871515249594955455549414861485947414740514548504842504053515141514047404958"
                    + "47574660465547604759485746464938483945414546474450404741484250394841474251415453"
                    + "46415246525150434943514052525144514050415040514051404940534050405040524052405040"
                    + "52405340534050404653494047415240474048384741494356434643454855395448534146444745"
                    + "55455252504049464850493952415240524053405140";

            MapVect[107] = "022  68.7  170.04742405550435140514051404940514052405140";

            MapVect[108] = "020  70.8  178.6485247414840504054395258516349555162";

            MapVect[109] = "310  69.4  178.05240534253385340534054405240585052414454503851365442553954585258"
                    + "48615746535753455257476047614959455048595256496142505161554655545561506047605458"
                    + "50405342614055445358495849605360526053605360546055605659455649595159515855604449"
                    + "52575456485553595856555757484846524554555749504657524652515758555745525955615951"
                    + "51595856575756603953405440524055404741414445465149394640454344434640454549424741"
                    + "45494147484048605259606155605360435144465358475851605460574954705857534152605060"
                    + "52605060464047604960496055535061486253544754506051585058536056605760566058605660"
                    + "566054585342484158494643565248414646504054445540485263475655565460496053";

            MapVect[110] = "050  49.0  142.04048395040553849494243534844594861486148614563444556495760555852"
                    + "56455745465857574351425043474253";

            MapVect[111] = "030  49.0  140.460586060605658605960586057605060456056605760605255585562";

            MapVect[112] = "048  45.2  141.64745574355445340484056525243556250575856485947595256515655425361"
                    + "4352444545505140455043454253";

            MapVect[113] = "106  41.5  141.15245565057456051565553555849575458485859435054585652455355545060"
                    + "48545649545755554457434749605460556151605344574958535957485743504447455844434445"
                    + "4440434150404940514146484141454557514642434243434146405043474642";

            MapVect[114] = "022  34.0  133.05148484647455245544955565058565343554445";

            MapVect[115] = "142  40.0  127.85952573957445747604958555258515955594153444441574842425651614654"
                    + "43484446465849585359535855545058464447524444454746555360576058585060565456425645"
                    + "54424238544448435748505754575557545656555840624361406050556254376054595661596155"
                    + "57565959576056585260525951605657526053575056594952554354";

            MapVect[116] = "018  20.0  110.041416653575453554660434944465042";

            MapVect[117] = "032  21.0  109.6435651605660606060536043574459406240624860486052595653595458";

            MapVect[118] = "018  22.0  120.841584250444642444941615361536055";

        }
        
        private ArrayList<String> checkRemainingBinFiles(ArrayList<String> binList) {
            ArrayList<String> allFiles = new ArrayList<>(binList);
            String workingDir = System.getProperty("user.dir");
            String downloadedDir = workingDir+"/BinDownloads/";
            File f = new File(downloadedDir);
            ArrayList<String> inhouseFileNames = new ArrayList<>(Arrays.asList(f.list()));
            allFiles.removeAll(inhouseFileNames);

            return allFiles;
        }
        
    class ColorCanvas extends Canvas implements MouseListener, KeyListener {
        int MouseX;
        int MouseY;
        private Dimension ScrSize;
        private Button MyButton;

        public ColorCanvas() {
            CenterLon = -900;
            DefKit = getToolkit();
            ScrSize = DefKit.getScreenSize();
            setSize((1024), (768));
            Ratio = (float) (800.0 / 1024.0);
            ScrSize = getSize();
            xLimMap = (int) ((float) this.getSize().width * Ratio);
            yLimMap = (int) ((float) this.getSize().height * Ratio);
            addMouseListener(this);
            addKeyListener(this);
        }

        public void setColor(Color color) {
            setBackground(color);
            repaint();
        }

        public void keyPressed(KeyEvent event) {
            String key = event.getKeyText(event.getKeyCode());
            if (Traces.size() > 0) {
                if (key.indexOf("Right") >= 0)
                {
                    Selected += 1;
                    if (Selected > Traces.size() - 1) {
                        Selected = 0;
                    }
                    cc.repaint();
                } else if (key.indexOf("Left") >= 0) {
                    Selected -= 1;
                    if (Selected < 0) {
                        Selected = Traces.size() - 1;
                    }
                    cc.repaint();
                }
            }
        }

        public void keyReleased(KeyEvent event) {}

        public void keyTyped(KeyEvent event) {}

        public void mouseMoved(MouseEvent event) {}

        public void mouseDragged(MouseEvent event) {}

        public void mouseEntered(MouseEvent event) {}

        public void mouseExited(MouseEvent event) {}

        public void mouseReleased(MouseEvent event) {}

        public void mousePressed(MouseEvent event) {
            MouseX = event.getX();
            MouseY = event.getY();
            if (MouseY > yLimMap) {
                if (Traces.size() > 0) {
                    if (MouseX / (xSize / Traces.size()) < Traces.size()) {
                        Selected = MouseX / (xSize / Traces.size());
                        cc.repaint();
                    }
                } else {
                    Selected = 0;
                }
                return;
            } else if ((MouseY <= yLimMap) && (MouseX <= xLimMap)) {
                TransmittedProfile pr;
                for (int i = 0; i < Traces.size(); i++) {
                    pr = Traces.get(i);

                    if (((pr.getxLocation() == MouseX) && (pr.getxLocation() == MouseX))
                            && ((pr.getyLocation() == MouseY) && (pr.getyLocation() == MouseY))) {
                        Selected = i;
                        cc.repaint();
                        return;
                    }
                }

                for (int i = 0; i < Traces.size(); i++) {
                    pr = Traces.get(i);
                    if (((pr.getxLocation() + 1 >= MouseX) && (pr.getxLocation() - 1 <= MouseX))
                            && ((pr.getyLocation() + 1 >= MouseY) && (pr.getyLocation() - 1 <= MouseY))) {
                        Selected = i;
                        cc.repaint();
                        return;
                    }
                }
            }
        }

        public void mouseClicked(MouseEvent event) {}

        public void paint(Graphics g) {
            Font font;
            int NoTraces = Traces.size();

            xLimMap = (int) ((float) getSize().width * .6);
            yLimMap = (int) ((float) getSize().height * Ratio);
            int xStart = getSize().width - (xLimMap + 1);
            int yStart = getSize().height - (yLimMap + 1);

            xSize = this.getSize().width;
            ySize = this.getSize().height;
            int ProfPosY = (yLimMap + 1);
            g.fillRect(0, 0, xLimMap, yLimMap);
            g.fillRect(xLimMap + 1, 0, xStart, yLimMap);
            g.fillRect(0, yLimMap + 1, getSize().width, yStart);
            g.setColor(Color.black);
            g.drawRect(xLimMap, 0, xStart, yLimMap);
            count++;

            int BXlat = 0, BXlon = 0, EXlat = 0, EXlon = 0;

            g.setColor(Color.green);
            for (int j = 0; j < 119; j++) {

                String x = MapVect[j];
                int la = (int) (Float.valueOf(x.substring(3, 9)).floatValue() * 10);
                int lo = (int) (Float.valueOf(x.substring(9, 16)).floatValue() * 10);

                la = 900 - la;
                lo = lo;
                if (lo < (-1800 - CenterLon)) {
                    lo = lo + 3600;
                }
                if (lo > (1800 - CenterLon)) {
                    lo = lo - 3600;
                }

                BXlat = XlateLat(la);
                BXlon = XlateLon(lo);

                for (int i = 16; i < x.length() - 4; i += 4) {

                    int LatOff = Integer.valueOf(x.substring(i, i + 2)).intValue();
                    int LonOff = Integer.valueOf(x.substring(i + 2, i + 4)).intValue();
                    int nla = la - (50 - LatOff);
                    int nlo = lo + (50 - LonOff);

                    nlo = nlo;
                    if (nlo < (-1800 - CenterLon)) {
                        nlo = nlo + 3600;
                    } else if (nlo > (1800 - CenterLon)) {
                        nlo = nlo - 3600;
                    }

                    EXlat = XlateLat(nla);
                    EXlon = XlateLon(nlo);
                    if ((Math.abs(BXlon - EXlon) < xLimMap / 2) && (Math.abs(nla - la) < 900)) {
                        g.setColor(Color.lightGray);
                        g.drawLine(BXlon, BXlat, EXlon, EXlat);
                    }
                    if ((EXlat > yLimMap) || (EXlon > xLimMap)) {
                        BXlat = EXlat;
                        BXlon = EXlon;
                        la = nla;
                        lo = nlo;
                    }
                    BXlat = EXlat;
                    BXlon = EXlon;
                    la = nla;
                    lo = nlo;

                };

            }

            g.setColor(Color.red);
            // Bottom panel - draw all profiles
            for (int i = 0; i < NoTraces; i++) {
                TransmittedProfile ThisOb = Traces.get(i);

                long ob = ThisOb.getObservationDateTime().getTime();
                long rec = ThisOb.getRecoveryDateTime().getTime();
                boolean Red = false;
                boolean BadCall = false;
                boolean DefRecTime = false;
                
                if (ThisOb.getRecoveryYear() == 1980) {
                    DefRecTime = true;
                }
                if (Math.abs((ob - rec)) / 3600000 >= 11)
                {
                    Red = true;
                }
                if (ThisOb.getProfile().getCallsign().indexOf('*') >= 0) {
                    BadCall = true;
                }
                int la = 900 - (int) (ThisOb.getProfile().getLatitude() * 10);
                int lo = (int) (ThisOb.getProfile().getLongitude() * 10);
                BXlat = XlateLat(la);
                BXlon = XlateLon(lo);
                ThisOb.setxLocation(BXlon);
                ThisOb.setyLocation(BXlat);
                if (BXlon > xLimMap) {
                    continue;
                }
                g.setColor(new Color(0, 192, 0));
                
//                if (Red && !DefRecTime) {
//                    g.setColor(Color.red);
//                }
                if (BadCall) {
                    g.setColor(Color.cyan);
                }
                g.drawLine(BXlon + 2, BXlat, BXlon - 2, BXlat);
                g.drawLine(BXlon, BXlat + 2, BXlon, BXlat - 2);

                DTPair Point;

                double Depth = MaxDepth + 100;
                int xBeg = this.getSize().width / NoTraces * i;
                int xWidth = this.getSize().width / NoTraces;

                if (ThisOb.getTemperaturePoints().size() > 0) {
                    Point = (DTPair) ThisOb.getTemperaturePoints().elementAt(0);
                    int x1 = (int) (xBeg + xWidth *
                            ((Point.getTemperatureMeasurement() + 2) / 37));
                    int y1 = ProfPosY + (int) ((ySize + 1 - yLimMap) *
                            Point.getDepthMeasurement() / Depth);

                    for (int j = 1; j < ThisOb.getTemperaturePoints().size(); j++) {
                        Point = (DTPair) ThisOb.getTemperaturePoints().elementAt(j);
                        int x2 = (int) (xBeg + xWidth *
                                ((Point.getTemperatureMeasurement() + 2) / 37));
                        int y2 = ProfPosY + (int) ((ySize + 1 - yLimMap) *
                                Point.getDepthMeasurement() / Depth);
                        g.drawLine(x1, y1, x2, y2);
                        x1 = x2;
                        y1 = y2;
                    }
                }
            }
            //border lines
            g.setColor(Color.RED);
            g.drawRect(xLimMap + 1, 0, xStart, yLimMap);
            g.drawLine(0, yLimMap + 1, this.getSize().width, yLimMap + 1);
            g.drawLine(xLimMap + 1, 0, xLimMap + 1, yLimMap);

            g.setColor(Color.white);

            if (Traces.size() > 0) {
                TransmittedProfile ThisOb = Traces.get(Selected);

                long ob = ThisOb.getObservationDateTime().getTime();
                long rec = ThisOb.getRecoveryDateTime().getTime();
                boolean Red = false;
                boolean BadCall = false;
                boolean DefRecTime = false;
                int Ryr = ThisOb.getRecoveryYear();
                if (Ryr == 80) {
                    DefRecTime = true;
                }

                if (Math.abs((ob - rec)) / 3600000 >= 11)
                {
                    Red = true;
                }
                if (ThisOb.getProfile().getCallsign().indexOf('*') >= 0) {
                    BadCall = true;
                }

                String Label = "Call Sign:";
                String out = ThisOb.getProfile().getCallsign();
                out += "  ";
                out += ThisOb.getProfile().getSoopLine();
                out += "  ";
                font = new Font("TimesRoman", Font.BOLD, 18);
                g.setFont(font);
                fm = getFontMetrics(font);
                int FHeight = fm.getHeight();
                int posY = FHeight;

                int SWidth = fm.stringWidth(Label);

                int posX = xLimMap + 10;
                g.setColor(new Color(0, 200, 200)); // teal
                g.drawString(Label, posX, posY);
                posX += SWidth + 10;
                g.setColor(Color.white);
                g.drawString(out, posX, posY);
                posX += fm.stringWidth(out);
                out = "NUM: ";
                g.setColor(new Color(0, 200, 200));
                g.drawString(out, posX, posY);
                
                posX += fm.stringWidth(out);
                out = Integer.toString(nobs);
                g.setColor(Color.white);
                g.drawString(out, posX, posY);

                posY += FHeight;
                posX = xLimMap + 10;
                Label = "Ob Time:";
                g.setColor(new Color(0, 200, 200)); // teal
                g.drawString(Label, posX, posY);
                out = ThisOb.profileDateTimeFormatted(
                        ThisOb.getObservationDateTime(), "GMT");
                g.setColor(Color.white);
                posX += SWidth + 10;
                g.drawString(out, posX, posY);
                posY += FHeight;
                posX = xLimMap + 10;
                Label = "Received:";
                g.setColor(new Color(0, 200, 200)); // teal
                g.drawString(Label, posX, posY);
                out = ThisOb.profileDateTimeFormatted(
                        ThisOb.getRecoveryDateTime(), "EST");
                g.setColor(Color.white);
//                if (Red && !DefRecTime && !BadCall) {
//                    g.setColor(Color.red);
//                }
                if (Red && DefRecTime && !BadCall) {
                    g.setColor(Color.white);
                }

                posX += SWidth + 10;
                g.drawString(out, posX, posY);

                posY += FHeight;
                posX = xLimMap + 10;
                Label = "POS:";
                g.setColor(new Color(0, 200, 200));
                g.drawString(Label, posX, posY);
                posX += SWidth + 10;
                DecimalFormat decform = new DecimalFormat("##000.00");
                out = decform.format(Math.abs(ThisOb.getProfile().getLatitude()));
                out += (ThisOb.getProfile().getLatitude() >= 0) ? " N" : " S";
                out += "  ";
                out += decform.format(Math.abs(ThisOb.getProfile().getLongitude()));
                out += (ThisOb.getProfile().getLongitude() >= 0) ? " E" : " W";
                g.setColor(Color.white);
                g.drawString(out, posX, posY);

                DecimalFormat decform2 = new DecimalFormat("#,##0.00");
                posY += FHeight;
                posX = xLimMap + 10;

                Label = "Distances:";
                g.setColor(new Color(0, 200, 200));
                g.drawString(Label, posX, posY);
                posX += SWidth + 10;

                out = "Prev ";
                g.setColor(new Color(56, 240, 32));
                g.drawString(out, posX, posY);
                posX += fm.stringWidth(out);

                out = decform2.format(ThisOb.getPreviousPOS());
                out += "km ";
                g.setColor(Color.white);
                g.drawString(out, posX, posY);
                posX += fm.stringWidth(out);

                out = "Next ";
                g.setColor(new Color(56, 240, 32));
                g.drawString(out, posX, posY);
                posX += fm.stringWidth(out);

                out = decform2.format(ThisOb.getNextPOS());
                out += "km";
                g.setColor(Color.white);
                g.drawString(out, posX, posY);

                // File
                posY += FHeight;
                posX = xLimMap + 10;
                Label = "File:";
                g.setColor(new Color(0, 200, 200));
                g.drawString(Label, posX, posY);
                posX += SWidth + 10;
                font = new Font("TimesRoman", Font.PLAIN, 14);
                g.setFont(font);
                out = ThisOb.getFileName();// File name
                g.setColor(Color.white);
                g.drawString(out, posX, posY);
                posY += FHeight;
                
                // Graph on rigth side panel
                int xbeginning = xLimMap + 43;
                double Depth = MaxDepth + 100;
                int yRemainder = yLimMap - posY;
                double NLab = yRemainder / (Depth / 100);
                font = new Font("Arial", Font.PLAIN, 12);
                g.setFont(font);
                DecimalFormat dec = new DecimalFormat("###");
                for (int i = 0; i < Depth; i += 100) {
                    out = dec.format(i);
                    // Vertical graph tick marks
                    g.drawLine(xbeginning,
                            (int) ((i / (float) Depth) * yRemainder + posY),
                            xLimMap + 37,
                            (int) ((i / (float) Depth) * yRemainder + posY));
                    // Vertical graph tick mark labels
                    if (posY + NLab * (i / 100) < yLimMap) {
                        g.drawString(out, xLimMap + 5,
                                (int) ((i / (float) Depth) * yRemainder + posY));
                    }
                }

                // Vertical graph line
                g.drawLine(xbeginning, posY, xbeginning,
                        (int) ((Depth / 100 * 100 / (float) Depth) * yRemainder + posY));
                int lowestTemp = (int) ((MinTemp < -3) ? MinTemp : -3);
                int highestTemp = (int) ((MaxTemp > 36) ? MaxTemp : 36);
                double TRange = (highestTemp + 1) - (lowestTemp - 1);

                g.setColor(Color.white);
                // Horizontal graph line
                g.drawLine(xbeginning, posY, this.getSize().width, posY);
                g.setColor(Color.white);

                for (int i = lowestTemp - 1; i < (int) highestTemp + 1; i++) {
                    if (i % 5 == 0) {
                        dec.applyPattern("#0");
                        out = dec.format(i);
                        int newxpos = (int) (((float) i - lowestTemp) / TRange *
                                (this.getSize().width - xbeginning) + xbeginning);
                        g.setColor(Color.white);
                        g.drawString(out, newxpos - 8, posY - 5);
                        g.setColor(Color.gray);
                        g.drawLine(newxpos, posY, newxpos, posY + yRemainder);
                    }
                }

                // Temperature profile on right side panel
                g.setColor(Color.yellow);
                TransmittedProfile trace = Traces.get(Selected);
                int FirstXpos = 0;
                int FirstYpos = 0;

                for (int i = 0; i < trace.getTemperaturePoints().size(); i++) {
                    DTPair pt = (DTPair) trace.getTemperaturePoints().elementAt(i);
                    int newxpos = (int) (
                            (pt.getTemperatureMeasurement() - lowestTemp) / TRange *
                            (this.getSize().width - xbeginning) + xbeginning);
                    int newypos = (int) (
                            (pt.getDepthMeasurement() / (float) Depth) *
                            yRemainder + posY);
                    if (i == 0) {
                        FirstXpos = newxpos;
                        FirstYpos = newypos;
                        g.drawLine(FirstXpos, FirstYpos, newxpos, newypos);
                    } else {
                        g.drawLine(FirstXpos, FirstYpos, newxpos, newypos);
                        FirstXpos = newxpos;
                        FirstYpos = newypos;
                    }

                }
                
                // Bottom panel draw selected profile
                if (Traces.size() > 0) {
                    ThisOb = Traces.get(Selected);
                    int la = 900 - (int) (ThisOb.getProfile().getLatitude() * 10);
                    int lo = (int) (ThisOb.getProfile().getLongitude() * 10);
                    BXlat = XlateLat(la);
                    BXlon = XlateLon(lo);
                    ThisOb.setxLocation(BXlon);
                    ThisOb.setyLocation(BXlat);
                    g.setPaintMode();
                    if (BXlon <= xLimMap) {

                        g.setColor(Color.yellow);
                        g.drawLine(BXlon + 4, BXlat, BXlon - 4, BXlat);
                        g.drawLine(BXlon, BXlat + 4, BXlon, BXlat - 4);

                        DTPair Point;

                        int xBeg = this.getSize().width / NoTraces * Selected;
                        int xWidth = this.getSize().width / NoTraces;

                        if (ThisOb.getTemperaturePoints().size() > 0) {
                            Point = (DTPair) ThisOb.getTemperaturePoints().elementAt(0);
                            int x1 = (int) (xBeg + xWidth *
                                    ((Point.getTemperatureMeasurement() + 2) / 37));
                            int y1 = ProfPosY + (int) ((float) (ySize + 1 - yLimMap) *
                                    (float) Point.getDepthMeasurement() / (float) Depth);
                            for (int j = 1; j < ThisOb.getTemperaturePoints().size(); j++) {
                                Point = (DTPair) ThisOb.getTemperaturePoints().elementAt(j);
                                int x2 = (int) (xBeg + xWidth *
                                        ((Point.getTemperatureMeasurement() + 2) / 37));
                                int y2 = ProfPosY + (int) ((float) (ySize + 1 - yLimMap) *
                                        (float) Point.getDepthMeasurement() / (float) Depth);
                                g.drawLine(x1, y1, x2, y2);
                                x1 = x2;
                                y1 = y2;
                            }
                        }
                    }
                }
            }
        }

        protected int XlateLat(int la) {
            return ((int) ((float) (la) / 1800.0 * yLimMap));
        }

        protected int XlateLon(int lo) {
            int x = ((int) ((float) ((lo) + (1800 - CenterLon)) / 3600.0 * xLimMap));
            if (x < 0) {
                x += xLimMap;
            }
            if (x > xLimMap) {
                x -= xLimMap;
            }
            return x;
        }
    }
}