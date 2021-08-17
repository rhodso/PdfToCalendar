/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhodso.main;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.awt.Desktop;
import java.net.URI;

/**
 *
 * @author rhodso
 */
public class mainGui extends javax.swing.JFrame {
    ArrayList<Event> addedEvents;
    boolean doExitAsk = false; 
   
    private void newListButtonActionPerformed(java.awt.event.ActionEvent evt) {
        addedEvents.clear();
        updateEventList();
        doExitAsk = false;
    }

    private void newEventButtonActionPerformed(java.awt.event.ActionEvent evt){ 
        //Launch the event Editor
        eventEdtior edtior = new eventEdtior(this);
        doExitAsk = true;
    }

    private void editEventButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(eventList.getSelectedIndex() != -1){
            int idx = eventList.getSelectedIndex();
            Event e = addedEvents.get(idx);
            addedEvents.remove(idx);
            eventEdtior edtior = new eventEdtior(this, e);
            doExitAsk = true;
        } else {
            JOptionPane.showMessageDialog(this, "No option selected!", "Cannot edit event", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {
        BufferedWriter br = null;
        try{
            //Setup and add headers
            ArrayList<String[]> strings = new ArrayList<>(0);
            String[] headers = {"Subject", "Start Date", "Start Time", "End Date", "End Time", "All day event", "Description", "Location"};
            strings.add(headers);
            
            //Add event 
            for(Event e : addedEvents){
                String[] data = new String[7];
                data[0] = e.getName();
                data[1] = e.getDate();
                data[3] = e.getDate();
                data[5] = "TRUE";
                data[6] = e.getDesc();
                strings.add(data);
            }

            //Now write the fucking CSV
            File f = new File("calendarOutput.csv");
            if(f.exists()){
                f.delete();
            }
            f.createNewFile();
            br = new BufferedWriter(new FileWriter(f));
            for(String[] line : strings){
                String toWrite = "";
                for(String s : line){
                    toWrite += s + ",";
                }
                toWrite = toWrite.substring(0, toWrite.length()-1);
                toWrite += "\n";
                br.write(toWrite);
            }
            //br.close is done in finally in case you go looking for it

        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Exception occured saving file\n" + e.getMessage() + "\n" + e.getStackTrace(), "Exception occured when saving", JOptionPane.OK_OPTION);
        } finally{
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Exception occured saving file. File may not have been saved corrently!\n" + e.getMessage() + "\n" + e.getStackTrace(), "Exception occured when saving", JOptionPane.OK_OPTION);
                }
            }
        }
        doExitAsk = false;
    }

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if(doExitAsk){
            if(JOptionPane.showConfirmDialog(this, "Current list not exported!\nAre you sure you want to exit?", "Confirm exit", JOptionPane.YES_NO_OPTION) == 0){
                this.dispose();
            }
        } else {
            this.dispose();
        }
    }

    private void secretButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Random random = new Random();
        if(random.nextInt(5) == 3){
            try{
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
                }
            } catch (Exception e){
                ;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Try again!", "Not yet", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void addEventToList(Event _e){
        //Add the event to list and update the gui list
        addedEvents.add(_e);
        updateEventList();
        doExitAsk = true;
    }

    public void updateEventList(){
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for(Event e : addedEvents){
            listModel.addElement(e.toString());
        }
        eventList.setModel(listModel);
        doExitAsk = true;
    }

    public void closeTheEditor(eventEdtior _edtior){
        _edtior.dispose();
    }

    public mainGui() {
        // Override default close operation
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
                public void windowClosing(WindowEvent e) {
                        dispose();
                }

                @Override
                public void windowActivated(WindowEvent arg0) {
                        ;
                }

                @Override
                public void windowClosed(WindowEvent arg0) {
                        ;
                }

                @Override
                public void windowDeactivated(WindowEvent arg0) {
                        ;
                }

                @Override
                public void windowDeiconified(WindowEvent arg0) {
                        ;
                }

                @Override
                public void windowIconified(WindowEvent arg0) {
                        ;
                }

                @Override
                public void windowOpened(WindowEvent arg0) {
                        ;
                }
        });

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        //Components etc
        initComponents();
        addedEvents = new ArrayList<Event>(0);
        updateEventList();
        this.setVisible(true);
        doExitAsk = false;
    }
    private void initComponents() {

        mainScrollPane = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        Header = new javax.swing.JLabel();
        controlsPanel = new javax.swing.JPanel();
        newListButton = new javax.swing.JButton();
        newEventButton = new javax.swing.JButton();
        editEventButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        secretButton = new javax.swing.JButton();
        listPanel = new javax.swing.JPanel();
        listHeader = new javax.swing.JLabel();
        listScroll = new javax.swing.JScrollPane();
        eventList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 700));

        mainPanel.setPreferredSize(new java.awt.Dimension(850, 570));

        Header.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        Header.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Header.setText("Calendar CSV Creator");

        newListButton.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        newListButton.setText("New list");
        newListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newListButtonActionPerformed(evt);
            }
        });

        newEventButton.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        newEventButton.setText("Create New Event");
        newEventButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newEventButtonActionPerformed(evt);
            }
        });

        editEventButton.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        editEventButton.setText("Edit Event");
        editEventButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEventButtonActionPerformed(evt);
            }
        });

        exportButton.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        exportButton.setText("Export to .CSV");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        exitButton.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        secretButton.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        secretButton.setText("Secret Button");
        secretButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                secretButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlsPanelLayout = new javax.swing.GroupLayout(controlsPanel);
        controlsPanel.setLayout(controlsPanelLayout);
        controlsPanelLayout.setHorizontalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newListButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newEventButton, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                    .addComponent(editEventButton, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                    .addComponent(exportButton, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                    .addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                    .addComponent(secretButton, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))
                .addContainerGap())
        );
        controlsPanelLayout.setVerticalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newListButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(newEventButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(editEventButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(secretButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        listHeader.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        listHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        listHeader.setText("Current Events:");

        eventList.setModel(new javax.swing.DefaultListModel<String>() {
            String[] strings = { "" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listScroll.setViewportView(eventList);

        javax.swing.GroupLayout listPanelLayout = new javax.swing.GroupLayout(listPanel);
        listPanel.setLayout(listPanelLayout);
        listPanelLayout.setHorizontalGroup(
            listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                    .addComponent(listScroll))
                .addContainerGap())
        );
        listPanelLayout.setVerticalGroup(
            listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listScroll)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(controlsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(listPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(controlsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(listPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        mainScrollPane.setViewportView(mainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private javax.swing.JLabel Header;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JButton editEventButton;
    private javax.swing.JList<String> eventList;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JLabel listHeader;
    private javax.swing.JPanel listPanel;
    private javax.swing.JScrollPane listScroll;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JButton newEventButton;
    private javax.swing.JButton newListButton;
    private javax.swing.JButton secretButton;
}
