package view;

import control.Controller;
import model.Game;
import model.Virologist;
import model.codes.GeneticCode;
import model.equipments.Equipment;
import model.map.Field;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//TODO a kezdokep
//  - megoldva egy simple update hivassal a drawcuccok vegen
//TODO comment
public class Window extends Observer{


    /**
     * Az utols� �zenet tartalma
     */
    String msgText;

    private JMenuBar menuBar;
    private JButton endButton;
    private JProgressBar aminoBar;
    private JProgressBar nucleoBar;

    //TODO 2 label a barhoz

    private JLabel aminoLabel;
    private JLabel nucleoLabel;
    private JLabel turnCounter;
    /**
     * Az �zenetbubor�k
     */
    private JLabel actionBubble;
    /**
     * Az �zenetbubor�k sz�vege
     */
    private JTextArea actionBubbleText;
    private ArrayList<JButton> equipments;

    private JLabel backGround;
    private JLayeredPane layeredPane;
    private JFrame frame;

    private Controller controller;
    private Game game;

    public Window(Controller controller, Game game){

        this.controller = controller;
        this.game = game;

        frame = new JFrame("Vilagtalan virologusok vilaga");
        JMenuBar mainMenu = new JMenuBar();
        JMenu actions = new JMenu("Actions");
        mainMenu.add(actions);
        frame.setJMenuBar(mainMenu);

        JMenu attack= new JMenu("attack");
        actions.add(attack);
        attack.addMenuListener(new ViewMenuListener(()->{
            attack.removeAll();
           Virologist v = game.GetCurrentPlayer();
            for (Virologist vir : v.getField().GetVirologists() ) {
                JMenuItem item = new JMenuItem(vir.getName());
                item.addActionListener((e)->controller.attack(vir));
                attack.add(item);
            }
        }));

        JMenu move= new JMenu("move");
        actions.add(move);
        move.addMenuListener(new ViewMenuListener(()->{
            move.removeAll();
            Virologist v = game.GetCurrentPlayer();
            for (Field field : v.getField().GetNeighbours() ) {
                JMenuItem item = new JMenuItem(field.getName());
                item.addActionListener((e)->controller.move(field));
                move.add(item);
            }
        }));

        JMenuItem drop = new JMenuItem("drop");
        actions.add(drop);
        drop.addActionListener((e)->controller.drop());

        JMenu lootAminoFrom = new JMenu("lootAminoFrom");
        actions.add(lootAminoFrom);
        lootAminoFrom.addMenuListener(new ViewMenuListener(()->{
            lootAminoFrom.removeAll();
            Virologist v = game.GetCurrentPlayer();
            for (Virologist vir : v.getField().GetVirologists() ) {
                if (!vir.equals(v)){
                    JMenuItem item = new JMenuItem(vir.getName());
                    item.addActionListener((e)->controller.lootAminoFrom(vir));
                    lootAminoFrom.add(item);
                }
            }
        }));

        JMenu lootNucleoFrom = new JMenu("lootNucleoFrom");
        actions.add(lootNucleoFrom);
        lootNucleoFrom.addMenuListener(new ViewMenuListener(()->{
            lootNucleoFrom.removeAll();
            Virologist v = game.GetCurrentPlayer();
            for (Virologist vir : v.getField().GetVirologists() ) {
                if (!vir.equals(v)){
                    JMenuItem item = new JMenuItem(vir.getName());
                    item.addActionListener((e)->controller.lootNucleoFrom(vir));
                    lootNucleoFrom.add(item);
                }
            }
        }));

        JMenu lootEquipmentFrom = new JMenu("lootEquipmentFrom");
        actions.add(lootEquipmentFrom);
        lootEquipmentFrom.addMenuListener(new ViewMenuListener(()->{
            lootEquipmentFrom.removeAll();
            Virologist v = game.GetCurrentPlayer();
            for (Virologist vir : v.getField().GetVirologists() ) {
                if (!vir.equals(v)){
                    JMenuItem item = new JMenuItem(vir.getName());
                    item.addActionListener((e)->controller.lootEquipmentFrom(vir));
                    lootEquipmentFrom.add(item);
                }
            }
        }));

        JMenuItem collect=new JMenuItem("collect");
        actions.add(collect);
        collect.addActionListener((e)->controller.collect());

        JMenuItem learn=new JMenuItem("learn");
        actions.add(learn);
        learn.addActionListener((e)->controller.learn());

        JMenuItem equip=new JMenuItem("equip");
        actions.add(equip);
        equip.addActionListener((e)->controller.equip());

        JMenu inject = new JMenu("inject");
        actions.add(inject);
        inject.addMenuListener(new ViewMenuListener(()->{
            inject.removeAll();
            Virologist v = game.GetCurrentPlayer();
            for (Virologist vir : v.getField().GetVirologists() ) {
                JMenu virMenu = new JMenu(vir.getName());
                virMenu.addMenuListener(new ViewMenuListener(()->{
                    virMenu.removeAll();
                    for (GeneticCode code : v.getGeneticCodes()){
                        JMenuItem codeItem = new JMenuItem(code.getName());
                        codeItem.addActionListener((e)->controller.inject(vir, code));
                        virMenu.add(codeItem);
                    }
                }));
                inject.add(virMenu);
            }
        }));

        JMenuItem endTurn=new JMenuItem("endTurn");
        actions.add(endTurn);
        endTurn.addActionListener((e) -> {
            controller.endTurn();
        });

        drawInterface();

        frame.setSize(600,600);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        update();
    }

    @Override
    public void update(){
        Virologist player = game.GetCurrentPlayer();

        //hatralevo korok szamanak frissitese
        turnCounter.setText(player.getActionCount() + " / 3");

        //allapotsavok frissitese
        nucleoBar.setValue(player.GetNucleotide() / player.GetMaterialLimit()*100);
        nucleoLabel.setText( String.valueOf(player.GetNucleotide()));
        aminoBar.setValue(player.GetAminoAcid() / player.GetMaterialLimit()*100);
        aminoLabel.setText(String.valueOf(player.GetAminoAcid()));

        //felszerelesek frissitese
        ArrayList<Equipment> equipment = player.GetEquipments();
        Image equipmentSlotIcon = null;
        for (int i = 0; i < 3; i++){
            if (equipment.size() > i) {
                Drawable drawableEquipment = (Drawable) equipment.get(i);
                try {
                    equipmentSlotIcon = ImageIO.read(new File(drawableEquipment.getTexture()));
                    equipmentSlotIcon = equipmentSlotIcon.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                } catch (IOException exception) { }
            } else {
                try {
                    equipmentSlotIcon = ImageIO.read(new File("textures/itemSlot.png"));
                    equipmentSlotIcon = equipmentSlotIcon.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                } catch (IOException ioException) { }
            }
            equipments.get(i).setIcon( new ImageIcon(equipmentSlotIcon));
        }

        if(!controller.getActionMessage().equals(""))
            msgText = player.getName()+": "+ controller.getActionMessage();


        if(msgText.equals("")){
            actionBubble.setVisible(false);
            actionBubbleText.setVisible(false);
        }
        else {
            actionBubbleText.setText(msgText);
            actionBubble.setVisible(true);
            actionBubbleText.setVisible(true);
        }

        //hatter frissitese
        Drawable drawableField = (Drawable) player.getField();
        Image backGroundIMG;
        //backGround;
        try {
            String in = drawableField.getTexture();
            backGroundIMG = ImageIO.read(new File(in));
            backGroundIMG = backGroundIMG.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
            backGround.setIcon(new ImageIcon(backGroundIMG));
            backGround.setBounds(0, 0, 600, 600);
        } catch (IOException e) { }
    }

    public void drawInterface() {
        layeredPane  = new JLayeredPane();
        layeredPane.setBounds(0, 0, 600, 600);

        Image endButtonIcon = null;
        try {
            endButtonIcon = ImageIO.read(new File("textures/endButton.png"));
            endButtonIcon = endButtonIcon.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        endButton = new JButton(new ImageIcon(endButtonIcon));
        endButton.setBorder(null);
        endButton.setContentAreaFilled(false);
        endButton.setBorderPainted(false);
        endButton.setBackground(Color.BLACK);
        endButton.setOpaque(false);
        endButton.setBounds(480, 450, 70, 70);

        nucleoBar = new JProgressBar();
        nucleoBar.setBounds(215, 460, 170, 25);
        nucleoLabel = new JLabel("min");
        nucleoLabel.setBounds(215, 465, 170, 15);
        nucleoLabel.setHorizontalAlignment(JLabel.CENTER);
        nucleoLabel.setFont(new Font("sans-serif", Font.BOLD, 13));

        aminoBar = new JProgressBar();
        aminoBar.setBounds(215, 495, 170, 25);
        aminoLabel = new JLabel("min");
        aminoLabel.setBounds(215, 500, 170, 15);
        aminoLabel.setHorizontalAlignment(JLabel.CENTER);
        aminoLabel.setFont(new Font("sans-serif", Font.BOLD, 13));

        Image equipmentSlotIcon = null;
        try {
            equipmentSlotIcon = ImageIO.read(new File("textures/itemSlot.png"));
            equipmentSlotIcon = equipmentSlotIcon.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        equipments = new ArrayList<>(3);
        int y = 340;
        for (int i = 0; i < 3; i++){
            JButton eq;
            eq = new JButton(new ImageIcon(equipmentSlotIcon));
            eq.setBounds(15, y, 50, 50);
            eq.setFocusPainted(false);
            eq.setBorderPainted(false);
            eq.setContentAreaFilled(false);
            eq.setBorder(null);
            equipments.add(eq);
            y += 60;
        }

        turnCounter = new JLabel("3 / 3");
        turnCounter.setFont(new Font("sans-serif", Font.BOLD, 48));
        turnCounter.setForeground(Color.white);
        turnCounter.setBounds(480, 25, 160, 50);

        Image actionBubbleIMG;
        ImageIcon actionBubbleIcon;
        try{
            actionBubbleIMG = ImageIO.read(new File("textures/bubble.png"));
            actionBubbleIMG = actionBubbleIMG.getScaledInstance(190, 88, Image.SCALE_SMOOTH);
            actionBubbleIcon = new ImageIcon(actionBubbleIMG);
            actionBubble = new JLabel(actionBubbleIcon);
            actionBubble.setBounds(280,225,190,88);
        } catch (IOException e) {
            e.printStackTrace();
        }

        msgText = "kisk�gya    kunya    g�gyuszka    kiskufya\n" +
                "kiskugya    k�tya    sulya    lislyuta\n" +
                "kizskugka    kutja    k�tja    kiskuhya\n" +
                "gizsgugya    kuta    kutyna    kiskuxya\n" +
                "gizskutya    kislyutya    kutnya    kiskuya\n" +
                "kiskutius    gidzsigegizsgutya    kitsikugya    kitsigugya\n" +
                "hutya    k�gyugya    lizya    k�gyja\n" +
                "kutsus    kogya    gidzsgudja    nyuta\n" +
                "gizsg�gya    kutttya    qgya    pimbull \n" +
                "g�tya    kizsgutya    kutgya    kugyja\n" +
                "kugyuzs    q�hggyah    qk�tgyikah    g�tjya\n" +
                "guggya    gizsgyugya    k�dtja    gizskugya\n" +
                "kuhya    k�ja    kudgya     tutya\n" +
                "g�gyah    kugyha    qutja    kislutya\n" +
                "kutsa    outya    kuyua    lizya\n" +
                "lutyw    litya    kitya    lutxa\n" +
                "kuxta    gidzsigegugyus    kuzxa    kik�gyka\n" +
                "g�tyja    kutxa    kigya    gugyuska\n" +
                "gisguya    kuxgy    kurya    gogya\n" +
                "kisgugytkya    jutya    kufya    gugklya\n" +
                "kiskulya    gizsgugyuzsga    kucsa    kiskytya\n" +
                "kismulya    guty    gizsgutyi    kiskhtya\n" +
                "kuva    vau    kizskugya    kiskjtya\n" +
                "qutya    k�gyka    kiskutja    kugyus\n" +
                "qugya    kuty    kulyuska    gutus \n" +
                "gisg�gya    lugya    kuxa    tugya\n" +
                "q�gya    g�gya    g�gyika    kutga\n" +
                "kuja    r�t valter (rottweiler)    gi gutga    kisgutya\n" +
                "kulya    kugta    kiekutya    kutuska\n" +
                "kucus    mutya    kiwkutya    nyutya\n" +
                "gizsigutygyja    gizs gugya    kiqkutya    kis kuta\n" +
                "kukia    g�zsg�gya    kikugya    kiskutyu\n" +
                "gyutya    qtya    kikutya    kutyuli\n" +
                "gutya    gigygugya    kiskurya     mutyuli\n" +
                "zuka    qkutya    kizskutga    kizskuja\n" +
                "guta    qtyja     gisgutgya    k�gy�s\n" +
                "zutyi    guka    gizsgutya    k�gya\n" +
                "k�nya    kuttya    tutus    kumgya\n" +
                "k�lya    putya    tuta    lutya\n" +
                "bidbulgugya    giszkutya    tyutyu    k�lya\n" +
                "qtja    kizsg�tya    tyutyus    gugyuzsga\n" +
                "k�j�k kis kugya    kizsgugya    kuthya    g�tyja\n" +
                "gugyus    gugyusga    kutyhus    gudgya\n" +
                "gizsgutga    kuya    gizsgutya    gizsgutya\n" +
                "bidbugugya    kisgugya    gutyna    gizsgutyus\n" +
                "kizskutja    kucuka    kismutya    ulya\n" +
                "gudja    kuszus    kutyulimutyuli    qty\n" +
                "kuzya    kutyha    kugdlya    g�tyja\n" +
                "kissqutya    k�gyka    kudglya    g�tygya\n" +
                "kissqtya    q�gya    dutya    kunyus \n" +
                "kiss kutya    kis kutyuss    kuyga    k�nya\n" +
                "kisk�gya    kuggya    gi guya    ksigugyq\n" +
                "kitzsikutynyuzska    kucs    kuryz    gizsgyutya\n" +
                "kislutyuy    giskunya    gizs kugy�gya    kisgucsa\n" +
                "kisg�gya    giskugyulimugyuli    gyutyulimugyuli    kurgya\n" +
                "gizs gudja    gisgugya    gisguya    kurtya\n" +
                "guttya     qutgya     quttya     kis lutyuj \n" +
                "glutya    gulytjya    kisluytuj     discsucsa\n" +
                "kiskz�uyta    kutjda    katya    lutyiluty\n" +
                "kutyika    kis kutsus    k�txa     kutxuzs\n" +
                "kuyly    kuyla    kiskunyus    gugyja\n" +
                "k�fka    k�dka    kissgugyuska    kisskugyus\n" +
                "k�tya    gidzsigutya    gunyus    kisgunyus\n" +
                "qs qtya    gugyuli-mugyuli    kizskzgya    k�tdja\n" +
                "krudja    krugyja    gizsguggya    kiskukia\n" +
                "kutyulu    kislutuy    kisgugyja    gutyja\n" +
                "kizs zsutyila    g�gyulka-mugyulka    kizsgudzsuka     kisgudzsus\n" +
                "kigy gyuka    k�qggyuzska    kusugyulj    qizs qtya\n" +
                "kufa    g�dzsus-mudzsus    kizs zslutya    q�dzsa\n" +
                "qugyka    gudzsuska    qk�tgya    kutguzska\n" +
                "kiskugy a     dicsakbuksi    qugyulimugyuli    tyutya\n" +
                "kisgutju    kisgy�gya    kigyugya    kisgugy";

        actionBubbleText = new JTextArea("Hello vak virologus!\n"+msgText);
        actionBubbleText.setBackground(Color.white);
        actionBubbleText.setBorder(BorderFactory.createEmptyBorder());
        actionBubbleText.setBounds(290,235, 170,50);
        actionBubbleText.setEditable(false);
        actionBubbleText.setFont(new Font("sans-serif", Font.BOLD, 12));
        actionBubbleText.setColumns(40);




        Image backGroundIMG;
        ImageIcon backGroundIcon;
        backGround = new JLabel();
        try {
            backGroundIMG = ImageIO.read(new File("textures/Field.png"));
            backGroundIMG = backGroundIMG.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
            backGroundIcon = new ImageIcon(backGroundIMG);
            backGround = new JLabel(backGroundIcon);
            backGround.setBounds(0, 0, 600, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }


        layeredPane.add(backGround, Integer.valueOf(0));
        layeredPane.add(actionBubble, Integer.valueOf(1));
        layeredPane.add(endButton, Integer.valueOf(1));
        layeredPane.add(aminoBar, Integer.valueOf(1));
        layeredPane.add(nucleoBar, Integer.valueOf(1));
        layeredPane.add(equipments.get(0), Integer.valueOf(1));
        layeredPane.add(equipments.get(1), Integer.valueOf(1));
        layeredPane.add(equipments.get(2), Integer.valueOf(1));
        layeredPane.add(turnCounter, Integer.valueOf(1));
        layeredPane.add(actionBubbleText, Integer.valueOf(2));
        layeredPane.add(nucleoLabel, Integer.valueOf(2));
        layeredPane.add(aminoLabel, Integer.valueOf(2));
        frame.add(layeredPane);
    }

}
