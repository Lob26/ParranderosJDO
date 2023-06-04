package edu.uniandes;

import edu.uniandes.business.ParranderosB;
import edu.uniandes.persistence.ParranderosP;
import edu.uniandes.view.ParranderosV;

public class Main {
    public static void main(String[] args)
            throws Exception {
        ParranderosP p = ParranderosP.getInstance();
        ParranderosB b = new ParranderosB(p);
        ParranderosV v = new ParranderosV(b);
        UIConfig.run(v);
        v.setVisible(true);
    }
}
