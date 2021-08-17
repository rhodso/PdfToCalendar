package com.rhodso.main;

public class launcher {
    public static void main(String[] args){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainGui m = new mainGui();
                m.setVisible(true);
            }
        });
    }
}
