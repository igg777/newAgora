package system.rdf.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

import model.ga.GeneticAlgorithmTemplate;
import model.ga.validation.ValidationData;
import model.user.User;

import org.apache.webdav.lib.properties.CurrentUserPrivilegeSetProperty;
import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultGraphCell;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.kswing.JKTreeMenu;

import com.hp.hpl.jena.rdf.model.Model;

import system.rbc.RBCMethods;
import system.rbc.RetrieveProcess;
import system.rdf.dataBase.ConnectionToPostgres;
import system.rdf.dataBase.PersistentManager;
import system.rdf.dataBase.Problem;
import system.rdf.dataBase.RDFSerializator;
import system.rdf.graph.ProblemGraph;
import system.rdf.graph.SolutionGraph;
import system.rdf.ontology.OntologyTree;
import system.rdf.ontology.OntologyTreeManager;
import system.rdf.utils.ImageGraph;
import system.rdf.utils.TabsComponentManager;
import system.rename.LoadProblemFromRDFFile;
import system.rename.SaveProblemToRDFFile;
import system.rename.Tools;
import view.enterprise.UIEnterpriseGestor;
import view.supplyChain.UISupplyChainInformationGestor;
import view.user.AuthenticateUser;
import view.user.UIUsersInfomationGestor;
import controller.ga.GeneticAlgorithmController;

public class GraphEd extends JFrame implements GraphSelectionListener, KeyListener {

	private JPanel contentPane;
	static JMenuItem crossOver = null;
	static Action insert, undo, redo, zoomin, zoom, zoomout, remove, group, ungroup;
	public static Tools tools = new Tools();
	static boolean flag = false;
	static OntologyTree ontTree = null;
	protected static JKTreeMenu tMenuOntology;
	private Component statusBar;
	private JTextPane insertedStatements;
	public static JTable tableInsertedStatement;
	public static JTabbedPane manageTabbedPane;
	private JScrollPane treePanel;
	private JSplitPane frame;
	private JToolBar toolbar;
	public static ArrayList<UIModeler> modelsss = new ArrayList<UIModeler>();
	static UIModeler model;
	public static JTabbedPane scenarios = new JTabbedPane();
	public static ErrorPane errorPane = null;
	public static String instalationPath = "D:/Deutch/development/NewAgora/";
	protected String PATH_SPLASHIMAGE = "system/rdf/resources/splash.jpg";
	private RDFSerializator rdfStorage;
	JMenu mnSystem;
	JMenu mnData;
	JMenu mnAnalyze;
	JMenu mnHelp;
	private User authenticatedUser;
	private String problemName = "";
	public int idToLoadAlternative = -1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Thread(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						AuthenticateUser authenticate = new AuthenticateUser();
						authenticate.setVisible(true);
						authenticate.setLocationRelativeTo(null);
					}
				});
			}
		}).start();
	}

	/**
	 * Create the frame.
	 */
	public GraphEd(User user) {
		this.authenticatedUser = user;
		Dimension d = new Dimension(750, 550);
		setTitle("New Agora");
		setSize(d);
		setPreferredSize(d);
		repaint();
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnData = new JMenu("Data");
		menuBar.add(mnData);

		JMenuItem mntmNewDecisionProblem = new JMenuItem("New decision problem");
		mntmNewDecisionProblem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model = createModel();
				model.addTpeditorChangeListener();
			}
		});
		mntmNewDecisionProblem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnData.add(mntmNewDecisionProblem);

		JMenu mnSaveDecisionalProblem = new JMenu("Save decisional problem");
		mnData.add(mnSaveDecisionalProblem);

		JMenuItem mntmSaveAsRdf = new JMenuItem("Save as RDF Problem");
		mntmSaveAsRdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeSaveProblem();
			}
		});
		mntmSaveAsRdf.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK));
		mnSaveDecisionalProblem.add(mntmSaveAsRdf);

		JMenuItem mntmSaveRdfTo = new JMenuItem("Save RDF to File");
		mntmSaveRdfTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeSaveRDFToFile();
			}
		});
		mntmSaveRdfTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnSaveDecisionalProblem.add(mntmSaveRdfTo);

		JMenuItem mntmSaveAsImage = new JMenuItem("Save as image");
		mntmSaveAsImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageSave();
			}
		});
		mntmSaveAsImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				InputEvent.CTRL_MASK));
		mnData.add(mntmSaveAsImage);

		JSeparator separator = new JSeparator();
		mnData.add(separator);

		JMenuItem mntmLoadProblem = new JMenuItem("Load Problem");
		mntmLoadProblem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeLoadProblem();
			}
		});
		mntmLoadProblem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				InputEvent.CTRL_MASK));
		mnData.add(mntmLoadProblem);

		JMenuItem mntmLoadProblemFrom = new JMenuItem(
				"Load Problem from RDF File");
		final GraphEd graphed = this;
		mntmLoadProblemFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeLoadProblemFromFile(graphed);
			}
		});
		mntmLoadProblemFrom.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_K, InputEvent.CTRL_MASK));
		mnData.add(mntmLoadProblemFrom);

		JMenuItem mntmLoadAlternative = new JMenuItem("Load Alternative");
		mntmLoadAlternative.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						executeLoadSlolution();
					}
				});
			}
		});
		mntmLoadAlternative.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_L, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnData.add(mntmLoadAlternative);

		JSeparator separator_1 = new JSeparator();
		mnData.add(separator_1);

		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		mnData.add(mntmUndo);

		JMenuItem mntmRedo = new JMenuItem("Redo");
		mntmRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				InputEvent.CTRL_MASK));
		mnData.add(mntmRedo);

		JSeparator separator_2 = new JSeparator();
		mnData.add(separator_2);

		JMenu mnGeneratingProblemsBy = new JMenu("Generating Problems by");
		mnData.add(mnGeneratingProblemsBy);

		JMenuItem mntmAlgorithmi = new JMenuItem("Algorithm (I)");
		mntmAlgorithmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeAlgorithmI();
			}
		});
		mntmAlgorithmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				InputEvent.CTRL_MASK));
		mnGeneratingProblemsBy.add(mntmAlgorithmi);

		JMenuItem mntmAlgortithmii = new JMenuItem("Algortithm (II)");
		mntmAlgortithmii.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeAlgorithmII();
			}
		});
		mntmAlgortithmii.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				InputEvent.CTRL_MASK));
		mnGeneratingProblemsBy.add(mntmAlgortithmii);

		mnAnalyze = new JMenu("Analyze");
		menuBar.add(mnAnalyze);

		JMenuItem mntmAnalyzeProblem = new JMenuItem("Analyze Problem");
		mntmAnalyzeProblem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeAnalyze();
			}
		});
		mntmAnalyzeProblem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnAnalyze.add(mntmAnalyzeProblem);

		JMenuItem mntmRbc = new JMenuItem("Possible Solution");
		mntmRbc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeRBC();
			}
		});
		mnAnalyze.add(mntmRbc);

		JSeparator separator_3 = new JSeparator();
		mnAnalyze.add(separator_3);

		mnSystem = new JMenu("System");
		menuBar.add(mnSystem);

		JMenuItem mntmConfigureConnection = new JMenuItem(
				"Configure Connection");
		mntmConfigureConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeConfigureConnection();
			}
		});
		mntmConfigureConnection.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnSystem.add(mntmConfigureConnection);

		JMenuItem mntmSettingSkin = new JMenuItem("Setting Skin");
		mntmSettingSkin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeSetting();
			}
		});
		mnSystem.add(mntmSettingSkin);

		JMenuItem mntmManageUsers = new JMenuItem("Manage Users");
		mntmManageUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeManageUsers();
			}
		});
		mntmManageUsers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				InputEvent.CTRL_MASK));
		mnSystem.add(mntmManageUsers);

		JMenuItem mntmManageEnterprises = new JMenuItem("Manage Enterprises");
		mntmManageEnterprises.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeManageEnterprises();
			}
		});
		mnSystem.add(mntmManageEnterprises);

		JMenuItem mntmManageSupplyChain = new JMenuItem("Manage Supply Chain");
		mntmManageSupplyChain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeManageChain();
			}
		});
		mnSystem.add(mntmManageSupplyChain);

		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmWelcome = new JMenuItem("Welcome");
		mntmWelcome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Doesn't have code
			}
		});
		mntmWelcome.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				InputEvent.CTRL_MASK));
		mnHelp.add(mntmWelcome);

		JMenuItem mntmUserManual = new JMenuItem("User Manual");
		mntmUserManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeUserManual();
			}
		});
		mntmUserManual
				.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnHelp.add(mntmUserManual);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				executeAbout();
			}
		});
		mnHelp.add(mntmAbout);

		populateContentPane();

		initSecurity();
	}

	// HERE BEGIN THE METHODS
	// METHODS' AREA

	/**
	 * Method for RBC
	 * 
	 */
	protected void executeRBC() {
		UIModeler modeler = ((UIModeler) scenarios.getSelectedComponent());
		if (modeler != null) {
			int selectedIndex = scenarios.getSelectedIndex();

			JGraph GraphTemp = ((UIModeler) scenarios.getSelectedComponent())
					.getEditorTabbedPane().getTabGraph();

			ProblemGraph problemgraph = (ProblemGraph) GraphTemp;
			
			if(!(((ProblemGraph) modelsss.get(0).getEditorTabbedPane()
					.getGraphs().get(0)).isSaved())){
				JOptionPane.showMessageDialog(new JFrame(), "Save the problem first");
				return;
			}

			try {
				problemgraph.classifyProblem();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			RetrieveProcess retrieveProcess = new RetrieveProcess();

			try {
				if (retrieveProcess.LoadDBProblems(problemgraph) == -1) {
					errorPane
							.printMessage(new Exception(
									"There aren't problems with this classification in the data base"));
				} else {
					Hashtable<ProblemGraph, String> retrievedProblems = retrieveProcess
							.Retrieve(problemgraph);
					if (retrievedProblems.size() != 0) {
						new UIRetrieveProblem(this, retrievedProblems).show();
					} else {
						errorPane.printMessage(new Exception(
								"There aren't retrieved problems"));
					}
				}
			} catch (HeadlessException | SQLException e) {
				// TODO Auto-generated catch block

			}

		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Formulate a problem");
		}
	}

	public static void ReloadProblems(String proName) {
		if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
			try {
				try {
					if (PersistentManager.getConnectionToPostgreSQL()
							.getProblems().size() == 0) {
						JOptionPane.showMessageDialog(new JFrame(),
								"No problems found!!!");
					} else {
						ConnectionToPostgres con = PersistentManager
								.getConnectionToPostgreSQL();
						;
						con.connect();
						Problem pro = con.getProblem(proName);
						ProblemGraph graph;
						// ArrayList<Problem> problems;
						// ConnectionToPostgres con =
						// PersistentManager.getConnectionToPostgreSQL();;
						// Problem pro;
						// con.connect();
						// problems = con.getProblems();
						// con.disconnect();
						// int pos = -1;
						// for (int i = 0; i < problems.size(); i++) {
						// if (problems.get(i).getName().equals(problemName))
						// pos = i;
						// }
						// pro = problems.get(pos);
						graph = new ProblemGraph();
						graph.insertProblem(pro);
						//Esto lo quite para no perder la alternativa al salvar el problema
//						modelsss.clear();
//						UIModeler model = createModel();
//						model.setTitle(pro.getName());
//						model.getEditorTabbedPane().loadProblem(pro);
//						model.addTpeditorChangeListener();
						refreshScenarios();
						// UILoadProblemSilent inst = new
						// UILoadProblemSilent(this, problemName);
					}

				}

				catch (NullPointerException e) {
					errorPane
							.printMessage(new Exception(
									"The file to configure the database is missing !!!"));
				}
			} catch (Exception e) {
				errorPane.printMessage(e);
			}
		} else if (errorPane.getText().equals(
				"The file to configure the database is missing !!!") == false)
			errorPane.printMessage(new Exception(
					"No database connection found!!"));
	}

	protected void executeAbout() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						AboutPanel about = new AboutPanel();
						about.setVisible(true);
						about.setLocationRelativeTo(null);
					}
				});
			}
		}).start();
	}

	protected void executeUserManual() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							File manual = new File("C:\\Users\\CHUCHI\\Documents\\THESIS\\AGora_2\\"+"manual.chm");
							Desktop esc = Desktop.getDesktop();
							esc.open(manual);
						} catch (IOException e) {
							// e.printStackTrace();
						}
					}
				});
			}
		}).start();
	}

	protected void executeManageChain() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UISupplyChainInformationGestor gestor = new UISupplyChainInformationGestor();
						gestor.setVisible(true);
						gestor.setLocationRelativeTo(null);
					}
				});
			}
		}).start();
	}

	protected void executeManageEnterprises() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIEnterpriseGestor gestor = new UIEnterpriseGestor();
						gestor.setVisible(true);
						gestor.setLocationRelativeTo(null);
					}
				});
			}
		}).start();
	}

	protected void executeManageUsers() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIUsersInfomationGestor gestor = new UIUsersInfomationGestor();
						gestor.setVisible(true);
						gestor.setLocationRelativeTo(null);
					}
				});
			}
		}).start();
	}

	protected void executeSetting() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UISettingSkin inst = new UISettingSkin(getJFrame());
						inst.setLocation(100, 150);
						inst.setVisible(true);
					}
				});
			}
		}).start();
	}

	protected void executeConfigureConnection() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						UIConfigureConnection inst = new UIConfigureConnection();
						inst.setLocation(100, 150);
						inst.setVisible(true);

					}
				});
			}
		}).start();
	}

	protected void executeAnalyze() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						ProblemGraph graph = (ProblemGraph) ((UIModeler) scenarios
								.getSelectedComponent()).getEditorTabbedPane()
								.getGraphs().get(0);
						if (graph == null) {
							errorPane.printMessage(new Exception(
									"Confeccione un grafo primero"));
						} else {
							if (graph.getModel().getRootCount() >= 1) {
								try {
									Tools tools = new Tools();
									tools.fillTable(graph);
								} catch (Exception e2) {
									errorPane.printMessage(e2);
								}
							}
						}
					}
				});
			}
		}).start();
	}

	protected void executeAlgorithmII() {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					GeneticAlgorithmTemplate alg = GeneticAlgorithmController
							.instance().runAlgorithm("Algorithm AG4");
					ValidationData data = (ValidationData) alg.getValidation()
							.getAssociatedData();
					JOptionPane
							.showMessageDialog(
									null,
									"Genetic Algorithm AGII has been applied successfully\n with following results.\n\n"
											+ "Generated Child Count : "
											+ data.getGeneratedChildCount()
											+ "\n"
											+ "Valic Child Count     : "
											+ data.getValidChildCount());
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		});
	}

	protected void executeAlgorithmI() {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					GeneticAlgorithmTemplate alg = GeneticAlgorithmController
							.instance().runAlgorithm("Algorithm AG3");
					ValidationData data = (ValidationData) alg.getValidation()
							.getAssociatedData();
					JOptionPane
							.showMessageDialog(
									null,
									"Genetic Algorithm AGI has been applied successfully\n with following results.\n\n"
											+ "Generated Child Count : "
											+ data.getGeneratedChildCount()
											+ "\n"
											+ "Valic Child Count     : "
											+ data.getValidChildCount());
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		});
	}

	protected void executeLoadProblemFromFile(final GraphEd graphed) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					String path = file.getAbsolutePath();
					LoadProblemFromRDFFile load = new LoadProblemFromRDFFile(
							path, graphed);
					try {
						load.createJenaModel();
						load.createGraph();
					} catch (Exception e) {
						// e.printStackTrace();
						errorPane.printMessage(e);
					}
				}
			}
		});
	}

	protected void executeSaveRDFToFile() {
		if (scenarios.getTabCount() > 0) {
			JGraph graphToRdf = ((UIModeler) scenarios.getSelectedComponent())
					.getEditorTabbedPane().getGraphs().get(0);
			if (graphToRdf.getModel().getRootCount() >= 1) {
				final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					String path = file.getAbsolutePath();
					// Iterate throw all node in graph
					// rdfStorage = new RDFSerializator(graphToRdf);
					// rdfStorage.saveModelLikeRdf(path + ".rdf");
					if (path.contains(".")) {
						path = path.substring(0, path.length() - 4);
					}
					path = path + ".owl";
					SaveProblemToRDFFile saveToRDF = new SaveProblemToRDFFile(
							graphToRdf, path);
					try {
						saveToRDF.createJenaModel();
						saveToRDF.getTriplesFromGraph();
						saveToRDF.addDataFromTriples();
						saveToRDF.writeData("RDF/XML");
					} catch (Exception e1) {
						e1.printStackTrace();
						errorPane.printMessage(e1);
					}
				}
			} else {
				JOptionPane.showMessageDialog(new JFrame(),
						"Formulate a problem");
			}
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Formulate a problem");
		}
	}

	/**
	 * This method populate the application
	 */
	protected void populateContentPane() {
		// Make disable the JMenuItem that allow the cross over
		if (crossOver != null)
			crossOver.setEnabled(false);

		// set the Background color
		getContentPane().setBackground(new Color(210, 210, 200));
		// Use Border Layout
		getContentPane().setLayout(new BorderLayout());
		// Add a ToolBar
		getContentPane().add(createToolBar(), BorderLayout.NORTH);
		// Add the Graph as Center Component
		// getContentPane().add(new JScrollPane(graph), BorderLayout.CENTER);
		statusBar = createStatusBar();

		getContentPane().add(statusBar, BorderLayout.SOUTH);
		getContentPane().add(createPanel(), BorderLayout.CENTER);
	}

	/**
	 * Create the tool bar
	 * 
	 * @return
	 */
	public JToolBar createToolBar() {

		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setBackground(new Color(210, 210, 200));
		String uri = "system/rdf/resources";
		final ClassLoader clsLoader = getClass().getClassLoader();

		// Insert
		URL insertUrl = clsLoader.getResource(uri + "/application_add.png");
		ImageIcon insertIcon = new ImageIcon(insertUrl);
		insert = new AbstractAction("insert", insertIcon) {
			public void actionPerformed(ActionEvent e) {
				insertActionPerformed(e);
			}
		};
		insert.setEnabled(true);
		toolbar.add(insert);

		// Undo
		toolbar.addSeparator();
		URL undoUrl = clsLoader
				.getResource("system/rdf/resources/arrow_rotate_clockwise.png");
		ImageIcon undoIcon = new ImageIcon(undoUrl);
		undo = new AbstractAction("", undoIcon) {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		};
		undo.setEnabled(true);
		toolbar.add(undo);

		// Redo
		URL redoUrl = clsLoader
				.getResource("system/rdf/resources/arrow_rotate_anticlockwise.png");
		ImageIcon redoIcon = new ImageIcon(redoUrl);
		redo = new AbstractAction("", redoIcon) {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		};
		redo.setEnabled(true);
		toolbar.add(redo);

		// Edit Block
		toolbar.addSeparator();

		URL removeUrl = clsLoader
				.getResource("system/rdf/resources/delete.gif");
		ImageIcon removeIcon = new ImageIcon(removeUrl);
		remove = new AbstractAction("", removeIcon) {
			public void actionPerformed(ActionEvent e) {
				executeRemove();
			}
		};
		remove.setEnabled(true);
		toolbar.add(remove);

		// Zoom Std
		toolbar.addSeparator();
		URL zoomUrl = clsLoader.getResource("system/rdf/resources/zoom.png");
		ImageIcon zoomIcon = new ImageIcon(zoomUrl);
		zoom = new AbstractAction("zoom", zoomIcon) {
			public void actionPerformed(ActionEvent e) {
				JGraph curr = ((UIModeler) scenarios.getSelectedComponent())
						.getEditorTabbedPane().getGraphHandler()
						.getCurrentGraph().get(0);
				curr.setScale(1.0);
			}
		};
		zoom.setEnabled(false);
		toolbar.add(zoom);

		// Zoom In
		URL zoomInUrl = clsLoader
				.getResource("system/rdf/resources/zoom_in.png");
		ImageIcon zoomInIcon = new ImageIcon(zoomInUrl);
		zoomin = new AbstractAction("", zoomInIcon) {
			public void actionPerformed(ActionEvent e) {

				JGraph curr = ((UIModeler) scenarios.getSelectedComponent())
						.getEditorTabbedPane().getGraphHandler()
						.getCurrentGraph().get(0);
				curr.setScale(1.1 * curr.getScale());

			}
		};
		zoomin.setEnabled(false);
		toolbar.add(zoomin);

		// Zoom Out
		URL zoomOutUrl = clsLoader
				.getResource("system/rdf/resources/zoom_out.png");
		ImageIcon zoomOutIcon = new ImageIcon(zoomOutUrl);
		zoomout = new AbstractAction("", zoomOutIcon) {
			public void actionPerformed(ActionEvent e) {
				JGraph curr = ((UIModeler) scenarios.getSelectedComponent())
						.getEditorTabbedPane().getGraphHandler()
						.getCurrentGraph().get(0);
				curr.setScale((curr.getScale() * 10) / 11);
			}
		};
		zoomout.setEnabled(false);
		toolbar.add(zoomout);

		// Group
		toolbar.addSeparator();
		URL groupUrl = clsLoader.getResource("system/rdf/resources/group.gif");
		ImageIcon groupIcon = new ImageIcon(groupUrl);
		group = new AbstractAction("", groupIcon) {
			public void actionPerformed(ActionEvent e) {
				ProblemGraph graph = (ProblemGraph) ((UIModeler) scenarios
						.getSelectedComponent()).getEditorTabbedPane()
						.getGraphs().get(0);
				group(graph.getSelectionCells());
			}
		};
		group.setEnabled(true);
		toolbar.add(group);

		// Ungroup
		URL ungroupUrl = clsLoader
				.getResource("system/rdf/resources/ungroup.gif");
		ImageIcon ungroupIcon = new ImageIcon(ungroupUrl);
		ungroup = new AbstractAction("", ungroupIcon) {
			public void actionPerformed(ActionEvent e) {
				ProblemGraph graph = (ProblemGraph) ((UIModeler) scenarios
						.getSelectedComponent()).getEditorTabbedPane()
						.getGraphs().get(0);
				ungroup(graph.getSelectionCells());
			}
		};
		ungroup.setEnabled(true);
		toolbar.add(ungroup);

		return toolbar;
	}// Fin de crear la barra de herramientas

	protected void executeRemove() {
		// TODO Auto-generated method stub
		UIModeler modeler = ((UIModeler) scenarios.getSelectedComponent());
		if (modeler != null) {
			ProblemGraph graph = (ProblemGraph) ((UIModeler) scenarios
					.getSelectedComponent()).getEditorTabbedPane().getGraphs()
					.get(0);
			if (!graph.isSelectionEmpty()) {
				((ProblemGraph) graph).removeCell();
			} else {
				JOptionPane.showMessageDialog(new JFrame(),
						"Select an item to remove");
			}
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Formulate a problem");
		}
	}

	/**
	 * Action performed of button insert
	 */
	public void insertActionPerformed(ActionEvent e) {
		// Create a UIModeler
		model = createModel();
		model.addTpeditorChangeListener();

	}

	/**
	 * Create a JInternalFrame that allow modeling a problem
	 */
	public static UIModeler createModel() {
		if (flag == false) {
			ontTree = ((OntologyTree) (tMenuOntology.getTree()));
			ontTree.activateListeners();
			flag = true;
			zoom.setEnabled(true);
			zoomin.setEnabled(true);
			zoomout.setEnabled(true);
		}
		model = new UIModeler();
		modelsss.add(model);
		refreshScenarios();
		scenarios.setSelectedIndex(modelsss.size() - 1);
		// graph = UITabbedPaneEditor.graphs.get(0);

		// scenarios.addTab("model" ,model);
		return model;

	}

	/**
	 * made by leo (test)
	 */

	public static void refreshScenarios() {
		int position = scenarios.getSelectedIndex();
		scenarios.removeAll();
		for (int i = 0; i < modelsss.size(); i++) {
			String name = ((ProblemGraph) modelsss.get(i).getEditorTabbedPane()
					.getGraphs().get(0)).getSavedName();
			if (name.equals("")) {
				name = "New  Problem";
			}
			scenarios.addTab(name, modelsss.get(i));
		}
		try {
			scenarios.setSelectedIndex(position);
		} catch (Exception e) {

		}
	}

	// public static void refreshScenarios() {
	// int position = scenarios.getSelectedIndex();
	// scenarios.removeAll();
	// for (int i = 0; i < modelsss.size(); i++) {
	// String name = ((ProblemGraph) modelsss.get(i).getEditorTabbedPane()
	// .getGraphs().get(0)).getSavedName();
	// if (name.equals("")) {
	// name = "New  Problem";
	// }
	// scenarios.addTab(name, modelsss.get(i));
	// }
	// try {
	// scenarios.setSelectedIndex(position);
	// } catch (Exception e) {
	//
	// }
	// }

	/**
	 * Returns the total number of cells in a graph
	 * 
	 * @param JGraph
	 * @return {@link Integer}
	 */
	/*
	 * protected int getCellCount(JGraph graph) { Object[] cells =
	 * graph.getDescendants(graph.getRoots()); return cells.length; }
	 */
	/**
	 * Ungroup the Groups in Cells and Select the Children
	 * 
	 * @param Object
	 *            []
	 */
	public void ungroup(Object[] cells) {
		ProblemGraph graph = (ProblemGraph) ((UIModeler) scenarios
				.getSelectedComponent()).getEditorTabbedPane().getGraphs()
				.get(0);
		graph.getGraphLayoutCache().ungroup(cells);
	}

	/**
	 * Delete all component paint on graph
	 * 
	 * @param graph
	 */

	/*
	 * protected void deleteModel(JGraph graph) {
	 * graph.getModel().remove(graph.getDescendants(graph.getRoots()));
	 * graph.getModel().remove(graph.getRoots()); }
	 * 
	 * 
	 * // Hook for subclassers public void installListeners(JGraph graph) { //
	 * Add Listeners to Graph
	 * 
	 * // Register UndoManager with the model
	 * graph.getModel().addUndoableEditListener(undoManager); // Update ToolBar
	 * based on Selection Changes
	 * graph.getSelectionModel().addGraphSelectionListener(this); // Listen for
	 * Delete Keystroke when the Graph has Focus graph.addKeyListener(this);
	 * graph.getModel().addGraphModelListener(statusBar); }
	 * 
	 * // Hook for subclassers protected void uninstallListeners(JGraph graph) {
	 * graph.getModel().removeUndoableEditListener(undoManager);
	 * graph.getSelectionModel().removeGraphSelectionListener(this);
	 * graph.removeKeyListener(this);
	 * graph.getModel().removeGraphModelListener(statusBar); }
	 * 
	 * // Hook for subclassers protected BasicMarqueeHandler
	 * createMarqueeHandler() { ProblemGraph graph = (ProblemGraph) ((UIModeler)
	 * scenarios .getSelectedComponent()).getEditorTabbedPane().getGraphs().get(
	 * 0); return new ProblemMarqueeHandler(graph); }
	 * 
	 * // Hook for subclassers protected DefaultGraphCell
	 * createDefaultGraphCell(String cellName) { DefaultGraphCell cell = new
	 * CustomCell(cellName); // Add one Floating Port cell.addPort(); return
	 * cell; }
	 * 
	 * // Hook for subclassers protected DefaultEdge createDefaultEdge() {
	 * return new DefaultEdge(); }
	 * 
	 * // Hook for subclassers public Map createEdgeAttributes() { Map map = new
	 * Hashtable(); // Add a Line End Attribute GraphConstants.setLineEnd(map,
	 * GraphConstants.ARROW_LINE); // GraphConstants.setRouting(map,
	 * GraphConstants.ROUTING_SIMPLE); // Fill end line
	 * GraphConstants.setEndFill(map, true); // Add a label along edge attribute
	 * GraphConstants.setLabelAlongEdge(map, true); return map; }
	 */
	// Create a Group that Contains the Cells
	public void group(Object[] cells) {
		ProblemGraph graph = (ProblemGraph) ((UIModeler) scenarios
				.getSelectedComponent()).getEditorTabbedPane().getGraphs()
				.get(0);
		// Order Cells by model Layering
		cells = graph.order(cells);
		// If Any Cells in view
		if (cells != null && cells.length > 0) {
			DefaultGraphCell group = createGroupCell();
			// Insert into model
			graph.getGraphLayoutCache().insertGroup(group, cells);
		}
	}

	// Hook for subclassers
	protected DefaultGraphCell createGroupCell() {
		return new DefaultGraphCell();
	}

	/**
	 * Redo the last Change to the model or the view
	 */
	public void redo() {
		try {
			UIModeler modeler = ((UIModeler) scenarios.getSelectedComponent());
			if (modeler != null) {
				// if the index is < than 0 then is a solutionGraph
				int selectedTab = ((UIModeler) scenarios.getSelectedComponent())
						.getEditorTabbedPane().getSelectedIndex();
				if (selectedTab == 0) {
					UndoManager undoManager = ((ProblemGraph) ((UIModeler) scenarios
							.getSelectedComponent()).getEditorTabbedPane()
							.getGraphs().get(0)).getUndoManager();
					if (undoManager.canRedo()) {
						undoManager.redo();
					}

				} else {
					((SolutionGraph) ((UIModeler) scenarios
							.getSelectedComponent()).getEditorTabbedPane()
							.getGraphs().get(selectedTab)).getUndoManager()
							.redo();

				}
			} else {
				JOptionPane.showMessageDialog(new JFrame(),
						"Formulate a problem");
			}

		} catch (Exception ex) {
			errorPane.printMessage(ex);
		}
		/*
		 * finally { updateHistoryButtons(); }
		 */
	}

	/**
	 * Undo the last Change to the model or the view
	 */
	public void undo() {
		try {
			UIModeler modeler = ((UIModeler) scenarios.getSelectedComponent());
			if (modeler != null) {
				// if the index is < than 0 then is a solutionGraph
				int selectedTab = ((UIModeler) scenarios.getSelectedComponent())
						.getEditorTabbedPane().getSelectedIndex();
				if (selectedTab == 0) {
					UndoManager undoManager = ((ProblemGraph) ((UIModeler) scenarios
							.getSelectedComponent()).getEditorTabbedPane()
							.getGraphs().get(0)).getUndoManager();
					if (undoManager.canUndo()) {
						undoManager.undo();
					}

				} else {
					((SolutionGraph) ((UIModeler) scenarios
							.getSelectedComponent()).getEditorTabbedPane()
							.getGraphs().get(selectedTab)).getUndoManager()
							.undo();
				}
			} else {
				JOptionPane.showMessageDialog(new JFrame(),
						"Formulate a problem");
			}

		} catch (Exception ex) {
			errorPane.printMessage(ex);
		}
		/*
		 * finally { updateHistoryButtons(); }
		 */
	}

	/**
	 * Create a status bar
	 */
	public StatusBarGraphListener createStatusBar() {
		return new EdStatusBar();
	}

	/**
	 * Create the Main split pane
	 * 
	 * @return JSplitPane
	 */
	public JSplitPane createPanel() {
		// loading Ontology in a JTree
		ontTree = OntologyTreeManager.getOntologyTree();

		// Create the Error Pane
		errorPane = TabsComponentManager.getErrorPaneInstance();
		// this is new leoooooo
		// create de insertedStatement panel
		insertedStatements = TabsComponentManager
				.getInsertedStatementInstance();
		tableInsertedStatement = TabsComponentManager
				.getTableInsertedStatement();
		tableInsertedStatement.setEnabled(false);

		// The TabbedPane that contains treePanel
		JTabbedPane ontologyTabbedPane = new JTabbedPane();
		// The TabbedPane that contains Tabs(Error, Properties table)
		manageTabbedPane = new JTabbedPane();

		tMenuOntology = new JKTreeMenu();
		tMenuOntology.setTree(ontTree);
		treePanel = new JScrollPane(tMenuOntology);
		ontologyTabbedPane.add("SCOR Ontology", treePanel);

		ontologyTabbedPane.setFocusable(false);

		manageTabbedPane.add("Errors", new JScrollPane(errorPane));
		manageTabbedPane.add("Asserted & Inferred Statements", new JScrollPane(
				tableInsertedStatement));
		manageTabbedPane.setFocusable(false);

		JSplitPane jsplit = new JSplitPane(0, scenarios, manageTabbedPane);

		// Create Split panel
		frame = new JSplitPane(1, ontologyTabbedPane, jsplit);

		// Set Splits panel properties
		frame.setOneTouchExpandable(true);
		frame.setDividerLocation(150);

		jsplit.setOneTouchExpandable(true);
		jsplit.setDividerLocation(350);
		// System.out.println(jsplit.getDividerLocation());
		Dimension minSize = new Dimension(100, 100);
		treePanel.setMinimumSize(minSize);

		// Fetch URL to Icon Resource
		URL jgraphUrl = GraphEd.class.getClassLoader().getResource(
				"org/jgraph/example/resources/jgraph.gif");
		// If Valid URL
		if (jgraphUrl != null) {
			// Load Icon
			ImageIcon jgraphIcon = new ImageIcon(jgraphUrl);
			// Use in Window
			// frame.setIconImage(jgraphIcon.getImage());

		}

		// Set dimension
		frame.setPreferredSize(new Dimension(150, 200));
		return frame;
	}

	/**
	 * 
	 * Execute save a problem
	 * 
	 * @param ActionEvent
	 */
	private void executeSaveProblem() {
		if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
			try {
				// getting the selected graph
				JGraph GraphToSave = ((UIModeler) scenarios
						.getSelectedComponent()).getEditorTabbedPane()
						.getTabGraph();
				if (GraphToSave instanceof ProblemGraph) {

					if (GraphToSave.getModel().getRootCount() >= 1) {
						if (((ProblemGraph) GraphToSave)
								.connectivitiInspector()) {
							Tools tools = new Tools();
							tools.fillTable(GraphToSave);
							ArrayList<String> cyclic = tools.isCyclic();
							// checking if the graph haven't any cyclic
							if (cyclic.size() == 0) {
								UISaveDecisionProblem save = new UISaveDecisionProblem(
										(ProblemGraph) GraphToSave);
								save.setModel((UIModeler) GraphEd.scenarios
										.getSelectedComponent());
								save.setLocationRelativeTo(null);

//								JGraph graphModeled = ((UIModeler) scenarios
//										.getSelectedComponent())
//										.getEditorTabbedPane().getTabGraph();
//								ProblemGraph newProblem = (ProblemGraph) graphModeled;

							} else {
								String toShow = "The problem can't be saved because there is a cyclic connection between this nodes \n";
								for (String string : cyclic) {
									toShow = toShow + string + "\n";
								}
								throw new Exception(toShow);
							}
						} else
							throw new Exception(
									"The problem can't be saved because is not connected correctly");
					}
				} else {
					// if enter here is because the graph is a SolutionGraph
					UISaveAlternative save = new UISaveAlternative();
					save.setGraph((SolutionGraph) GraphToSave);
				}
			} catch (CycleFoundException e1) {
				GraphEd.errorPane.printMessage(new Exception(
						"Error, exist a cyclic connection in the graph"));
			} catch (Exception e2) {
				errorPane.printMessage(e2);
			}
		} else {
			this.errorPane.printMessage(new Exception(
					"No database connection detected"));
		}
	}

	/**
	 * Saves the graph as an image in jpg format
	 * 
	 */
	public void imageSave() {

		UIModeler modeler = ((UIModeler) scenarios.getSelectedComponent());
		if (modeler != null) {
			JFileChooser ch = new JFileChooser();
			ch.setDialogTitle("Export Image");
			ch.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filterJPG = new FileNameExtensionFilter(
					"JPG", "JPG");
			ch.addChoosableFileFilter(filterJPG);
			FileNameExtensionFilter filterPNG = new FileNameExtensionFilter(
					"PNG", "PNG");
			ch.addChoosableFileFilter(filterPNG);
			// ch.setFileFilter(new ImageFilter());
			// ch.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			// int op = ch.showDialog(new JPanel(), "Export Image");
			int op = ch.showSaveDialog(this);
			if (op == JFileChooser.APPROVE_OPTION) {
				try {
					File f = ch.getSelectedFile();
					String dir = f.getPath();
					if (ch.getFileFilter().getDescription().equals("JPG")) {
						ImageGraph.writeJPG(modeler.getEditorTabbedPane()
								.getGraphs().get(0), dir + ".jpg");
					} else if (ch.getFileFilter().getDescription()
							.equals("PNG")) {
						ImageGraph.writePNG(modeler.getEditorTabbedPane()
								.getGraphs().get(0), dir + ".png");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Formulate a problem");
		}
	}

	public void executeLoadProblem() {
		if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
			try {
				try {
					if (PersistentManager.getConnectionToPostgreSQL()
							.getProblems().size() == 0) {
						JOptionPane.showMessageDialog(this,
								"No problems found!!!");
					} else {
						UILoadProblem inst = new UILoadProblem(this);
						inst.setLocationRelativeTo(null);
						inst.setVisible(true);
					}

				}

				catch (NullPointerException e) {
					this.errorPane
							.printMessage(new Exception(
									"The file to configure the database is missing !!!"));
				}
			} catch (Exception e) {
				this.errorPane.printMessage(e);
			}
		} else if (errorPane.getText().equals(
				"The file to configure the database is missing !!!") == false)
			this.errorPane.printMessage(new Exception(
					"No database connection found!!"));
	}

	public void executeLoadSlolution() {	
		if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
			try {
				try {
					if (PersistentManager.getConnectionToPostgreSQL()
							.getSolutions().size() == 0) {
						JOptionPane.showMessageDialog(this,
								"No alternatives found!!!");
					} else {
						UILoadAlternative inst = new UILoadAlternative(this);
						inst.setVisible(true);
						inst.setLocationRelativeTo(null);
					}
				}

				catch (NullPointerException e) {
					this.errorPane
							.printMessage(new Exception(
									"The file to configure the database is missing !!!"));
				}
			} catch (Exception e) {
				this.errorPane.printMessage(e);
			}
		} else if (errorPane.getText().equals(
				"The file to configure the database is missing !!!") == false)
			this.errorPane.printMessage(new Exception(
					"No database connection found!!"));
		
		UIModeler modeler = ((UIModeler) scenarios.getSelectedComponent());
		if (modeler != null) {
			int selectedIndex = scenarios.getSelectedIndex();

			JGraph GraphTemp = ((UIModeler) scenarios.getSelectedComponent())
					.getEditorTabbedPane().getTabGraph();

			ProblemGraph problemgraph = (ProblemGraph) GraphTemp;

			if (PersistentManager.getConnectionToPostgreSQL().tryConnection()) {
				try {
					try {
						if (PersistentManager
								.getConnectionToPostgreSQL()
								.getAlternativesForProblems(
										problemgraph.getProblem_list().get(0)
												.getId()).size() == 0) {
							JOptionPane.showMessageDialog(this,
									"No alternatives found!!!");
						} else {
							this.idToLoadAlternative = problemgraph.getProblem_list().get(0).getId();
							if(idToLoadAlternative != -1){
								UILoadAlternative inst = new UILoadAlternative(this);
								inst.setVisible(true);
								inst.setLocationRelativeTo(null);
							}
						}
					}

					catch (NullPointerException e) {
						this.errorPane
								.printMessage(new Exception(
										"The file to configure the database is missing !!!"));
					}
				} catch (Exception e) {
					this.errorPane.printMessage(e);
				}
			} else if (errorPane.getText().equals(
					"The file to configure the database is missing !!!") == false)
				this.errorPane.printMessage(new Exception(
						"No database connection found!!"));

			// PersistentManager.getConnectionToPostgreSQL().getAlternativesForProblems(problemgraph.getProblem_list().get(0).getId());
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Formulate a problem");
		}
		
	}

	private JFrame getJFrame() {
		return this;
	}

	public void exit() {
		System.exit(0);
	}

	public void initSecurity() {
		if (authenticatedUser.getRole().toString().equals("Administrator")) {
			mnData.setVisible(false);
			mnAnalyze.setVisible(false);
			mnData.setEnabled(false);
			mnAnalyze.setEnabled(false);
			toolbar.hide();
			frame.hide();
		} else if (authenticatedUser.getRole().toString().equals("Analist")) {
			mnSystem.setVisible(false);
			mnSystem.setEnabled(false);
		}
	}

	// HERE BEGIN OVERRIDES METHODS

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void valueChanged(GraphSelectionEvent arg0) {
	}

}
