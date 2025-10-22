package com.luciaia.cochecomboboxxml;

import com.luciaia.cochecomboboxxml.base.Animal;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Vista {
    private JFrame frame;

    // creados al arrastrar
    private JPanel panel1;
    private JTextField nombreTxt;
    private JTextField especieTxt;
    private JComboBox comboBox1;
    private JButton altaAnimalButton;
    private JButton mostrarAnimalButton;
    private JLabel lblAnimal;

    // dos elementos más creados por mí
    private LinkedList<Animal> lista;
    private DefaultComboBoxModel dcbm;
    // necesarios para guardar datos de los animales
    // y para poner cosas gráficas

    // boton derecha generate form main()
    // lo cambio para que sea un constructor
    public Vista() {
        // elimino la creación del JFrame
        frame = new JFrame("Vista");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // añado un método más
        crearMenu();
        frame.setVisible(true);

        // central
        frame.setLocationRelativeTo(null);
        // inicializo linkedList y DefaultComboBoxModel
        lista = new LinkedList<>();
        dcbm = new DefaultComboBoxModel();
        // aplico el comobox el modelo DefaultComboBoxModel
        comboBox1.setModel(dcbm);

        //
        altaAnimalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                altaAnimal(nombreTxt.getText(), especieTxt.getText());
                // Limpiamos datos
                refrescarComboBox();
            }
        });

        comboBox1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                // cuando le de a suprimir
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    lista.remove(dcbm.getSelectedItem());
                    refrescarComboBox();
                }
            }
        });

        mostrarAnimalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //muestro el Animal seleccionado en la label
                Animal seleccionado = (Animal) dcbm.getSelectedItem();
                lblAnimal.setText(seleccionado.toString());
            }
        });
    }

    private void refrescarComboBox() {
        dcbm.removeAllElements();
        for (Animal coche : lista) {
            dcbm.addElement(coche);
        }
    }

    private void crearMenu() {
        JMenuBar barra = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        JMenuItem itemExportarXML = new JMenuItem("Exportar XML");
        JMenuItem itemImportarXML = new JMenuItem("Importar XML");

        itemImportarXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser selectorArchivo = new JFileChooser();
                int opcionSeleccionada = selectorArchivo.showOpenDialog(null);

                if (opcionSeleccionada == JFileChooser.APPROVE_OPTION) {
                    File fichero = selectorArchivo.getSelectedFile();
                    importarXML(fichero);
                    refrescarComboBox();
                }
            }
        });

        itemExportarXML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser selectorArchivo = new JFileChooser();
                int opcionSeleccionada = selectorArchivo.showSaveDialog(null);

                if (opcionSeleccionada == JFileChooser.APPROVE_OPTION) {
                    File fichero = selectorArchivo.getSelectedFile();
                    exportarXML(fichero);
                }
            }
        });

        menu.add(itemImportarXML);
        menu.add(itemExportarXML);

        barra.add(menu);
        frame.setJMenuBar(barra);

    }

    private void importarXML(File fichero) {
        // Clases para construir un XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document documento = builder.parse(fichero);

            // recorrer cada uno de los nodos coche para obtener sus campos
            // Nodo padre en el documento XML
            NodeList coches = documento.getElementsByTagName("coche");
            for (int i = 0; i < coches.getLength(); i++) {
                Node coche = coches.item(i);
                Element elemento = (Element) coche;

                // Obtengo los campos nombre y especie
                String nombre = elemento.getElementsByTagName("nombre").item(0).getChildNodes().item(0).getNodeValue();
                String especie = elemento.getElementsByTagName("especie").item(0).getChildNodes().item(0).getNodeValue();

                altaAnimal(nombre, especie);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void exportarXML(File fichero) {
        // Clases para construir un XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        try {
            builder = factory.newDocumentBuilder();

            // carga lo necesario para construir un XML
            DOMImplementation dom = builder.getDOMImplementation();

            // creo documento que representa XML
            Document documento = dom.createDocument(null, "xml", null);
            // creo elnodo raiz (Coches) y lo añado al documento
            Element raiz = documento.createElement("coches");
            documento.getDocumentElement().appendChild(raiz);

            Element nodoCcohe;
            Element nodoDatos;
            Text dato;

            // por cada coche de la lista, creo un nodo coche
            for (Animal coche : lista) {
                // creon un nodo coche y lo añado al nodo raiz
                nodoCcohe = documento.createElement("coche");
                raiz.appendChild(nodoCcohe);

                // a cada coche le añado los nodos marac y coche
                nodoDatos = documento.createElement("nombre");
                nodoCcohe.appendChild(nodoDatos);

                // a cada nodo datos le añado el contenido
                dato = documento.createTextNode(coche.getNombre());
                nodoDatos.appendChild(dato);

                nodoDatos = documento.createElement("especie");
                nodoCcohe.appendChild(nodoDatos);

                dato = documento.createTextNode(coche.getEspecie());
                nodoDatos.appendChild(dato);
            }

            // transformo el documento
            Source src = new DOMSource(documento);
            Result result = new StreamResult(fichero);

            Transformer transformer = null;
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(src, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void altaAnimal(String nombre, String especie) {
        lista.add(new Animal(nombre, especie));
    }

    public static void main(String[] args) {
        Vista vista = new Vista();
    }
}
