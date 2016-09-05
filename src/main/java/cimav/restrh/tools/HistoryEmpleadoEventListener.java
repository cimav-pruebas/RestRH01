/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cimav.restrh.tools;


import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.persistence.descriptors.DescriptorEvent;
import org.eclipse.persistence.descriptors.DescriptorEventAdapter;
import org.eclipse.persistence.queries.UpdateObjectQuery;
import org.eclipse.persistence.sessions.changesets.ChangeRecord;

/**
 *
 * @author calderon
 */
public class HistoryEmpleadoEventListener extends DescriptorEventAdapter {

    @Override
    public void postUpdate(DescriptorEvent event) {
        
        if (true) return;
        
        // TODO Codigo para el Audit en MongoDB
        /*
        audit: {
        year:
        quincena:
        table:
        id:
        attr:
        old:
        new:
        user:
        timestamp:
        }
        
        */
    
        /*
        UpdateObjectQuery query = (UpdateObjectQuery) event.getQuery();
        for (ChangeRecord changeRecord : query.getObjectChangeSet().getChanges()) {
            try {
                Object attr = changeRecord.getAttribute();
                Object id = query.getObjectChangeSet().getId();
                Object newVal = PropertyUtils.getProperty(query.getObject(), changeRecord.getAttribute());
                Object oldVal = changeRecord.getOldValue();
                
                System.out.println("ObjectChangeSet: " + attr + " : " + id + " : " + newVal + " : " + oldVal);
                Logger.getLogger(HistoryEmpleadoEventListener.class.getName()).log(Level.INFO, null, " TexMex: " + attr + " : " + id + " : " + newVal + " : " + oldVal);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(HistoryEmpleadoEventListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
*/
        
    }
    
}
