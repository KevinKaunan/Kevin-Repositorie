package vistacontrolador;

import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.AlumnoNoExistenteExcepcion;
import modelo.CorrectorProyectos;

/*
* @author Kevin Klein
*/

public class GuiCorrectorProyectos extends Application
	{

		private MenuItem itemLeer;
		private MenuItem itemGuardar;
		private MenuItem itemSalir;

		private Label lblEntrada;
		private Label lblOpciones;
		private Label lbAlumno;
		private TextField txtAlumno;
		private Button btnVerProyecto;

		private RadioButton rbtAprobados;
		private RadioButton rbtOrdenados;
		private Button btnMostrar;

		private TextArea areaTexto;

		private Button btnClear;
		private Button btnSalir;

		private CorrectorProyectos corrector; // el modelo

		@Override
		public void start(Stage stage)
		{

			corrector = new CorrectorProyectos();

			BorderPane root = crearGui();

			Scene scene = new Scene(root, 800, 600);
			stage.setScene(scene);
			stage.setTitle("- Corrector de proyectos -");
			scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
			stage.show();
		}

		private BorderPane crearGui()
		{

			BorderPane panel = new BorderPane();
			MenuBar barraMenu = crearBarraMenu();
			panel.setTop(barraMenu);

			VBox panelPrincipal = crearPanelPrincipal();
			panel.setCenter(panelPrincipal);

			HBox panelBotones = crearPanelBotones();
			panel.setBottom(panelBotones);

			return panel;
		}

		private MenuBar crearBarraMenu()
		{

			MenuBar barraMenu = new MenuBar();
			barraMenu.getStyleClass().add("menu-bar");
			Menu menu = new Menu("Archivo");
			menu.getStyleClass().add("menu");
			// item leer
			itemLeer = new MenuItem("_Leer de fichero");
			itemLeer.setAccelerator(KeyCombination.keyCombination("CTRL+L"));
			itemLeer.setOnAction(event -> leerDeFichero());

			// item guardar
			itemGuardar = new MenuItem("_Guardar en fichero"); // deshabilitarlo
			itemGuardar.setAccelerator(KeyCombination.keyCombination("CTRL+G"));
			itemGuardar.setDisable(true);
			itemGuardar.setOnAction(event -> salvarEnFichero());
			// item salir
			itemSalir = new MenuItem("_Salir");
			itemSalir.setAccelerator(KeyCombination.keyCombination("CTRL+S"));
			itemSalir.setOnAction(event -> salir());
			menu.getItems().addAll(itemLeer, itemGuardar);
			menu.getItems().addAll(new SeparatorMenuItem(), itemSalir);
			barraMenu.getMenus().add(menu);
			return barraMenu;

		}

		private VBox crearPanelPrincipal()
		{

			VBox panel = new VBox();
			panel.setPadding(new Insets(5));
			panel.setSpacing(10);
			lblEntrada = new Label("Panel de entrada");
			lblEntrada.getStyleClass().add("titulo-panel");
			lblEntrada.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			lblOpciones = new Label("Panel de opciones");
			lblOpciones.getStyleClass().addAll("titulo-panel");
			lblOpciones.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			areaTexto = new TextArea();
			areaTexto.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			VBox.setVgrow(areaTexto, Priority.ALWAYS);
			panel.getChildren().addAll(lblEntrada, crearPanelEntrada(), lblOpciones, crearPanelOpciones(), areaTexto);
			return panel;
		}

		private HBox crearPanelEntrada()
		{

			HBox panel = new HBox();
			panel.setPadding(new Insets(5));
			panel.setSpacing(10);
			lbAlumno = new Label("Alumno");
			lbAlumno.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
			txtAlumno = new TextField();
			txtAlumno.setPrefColumnCount(30);
			txtAlumno.setOnAction(event -> verProyecto());
			btnVerProyecto = new Button("Ver Proyecto");
			btnVerProyecto.setPrefWidth(120);
			btnVerProyecto.setOnAction(event -> verProyecto());
			panel.getChildren().addAll(lbAlumno, txtAlumno, btnVerProyecto);

			return panel;
		}

		private HBox crearPanelOpciones()
		{

			HBox panel = new HBox();
			panel.setPadding(new Insets(5));
			panel.setSpacing(50);
			panel.setAlignment(Pos.CENTER);
			// botones radio
			rbtAprobados = new RadioButton("Mostrar aprobados");
			rbtAprobados.setSelected(true);
			rbtOrdenados = new RadioButton("Mostrar ordenados");
			ToggleGroup grupo = new ToggleGroup();
			rbtAprobados.setToggleGroup(grupo);
			rbtOrdenados.setToggleGroup(grupo);
			// boton
			btnMostrar = new Button("Mostrar");
			btnMostrar.setOnAction(event -> mostrar());
			panel.getChildren().addAll(rbtAprobados, rbtOrdenados, btnMostrar);
			return panel;
		}

		private HBox crearPanelBotones()
		{

			HBox panel = new HBox();
			panel.setPadding(new Insets(5));
			panel.setSpacing(10);
			btnClear = new Button("Clear");
			btnClear.setPrefWidth(90);
			btnClear.setOnAction(event -> clear());
			btnSalir = new Button("Salir");
			btnSalir.setPrefWidth(90);
			btnSalir.setOnAction(event -> salir());
			panel.setAlignment(Pos.BOTTOM_RIGHT);
			panel.getChildren().addAll(btnClear, btnSalir);
			return panel;
		}

		private void salvarEnFichero()
		{
			try
			{
				corrector.guardarOrdenadosPorNota();
				areaTexto.setText("Guardados en fichero de texto los proyectos ordenados");
			} catch (IOException e)
			{
				System.out.println("Error de io" + e.getMessage());
			}

		}

		private void leerDeFichero()
		{
			corrector.leerDatosProyectos();
			areaTexto.setText("Leido de fichero \n" + corrector.getErrores().toString()
					+ "\nYa están guardados en memoria los datos de los proyectos");
			itemLeer.setDisable(true);
			itemGuardar.setDisable(false);
		}

		private void verProyecto()
		{
			clear();
			if (itemLeer.isDisable())
			{
				try
				{

					String nomAlumno = txtAlumno.getText();
					if (!nomAlumno.isEmpty())
					{
						areaTexto.setText(corrector.proyectoDe(nomAlumno).toString());
					} else
					{
						areaTexto.setText("Teclee nombre de alumno");

					}
				} catch (AlumnoNoExistenteExcepcion e)
				{
					areaTexto.setText(e.toString());
				}

			} else
			{
				areaTexto.setText("No se ha leido ningún fichero todavía");
			}

		}

		private void mostrar()
		{

			clear();
			if (!rbtAprobados.isSelected())
			{
				areaTexto.setText(corrector.toString());
			} else
			{
				areaTexto.setText("Han aprobado el proyecto " + corrector.aprobados() + " alumnos/as");
			}

		}

		private void cogerFoco()
		{

			txtAlumno.requestFocus();
			txtAlumno.selectAll();

		}

		private void salir()
		{

			System.exit(0);
		}

		private void clear()
		{

			areaTexto.clear();
			cogerFoco();
		}

		public static void main(String[] args)
		{

			launch(args);
		}
	}