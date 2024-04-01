package edu.iut.proftracker.models;

import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import edu.iut.proftracker.views.activitites.NotificationActivity;

/** Classe Notification permettant de gérer les notifications représentant les rendez-vous entre un professeur et un élève

 */

public class Notification {
    private static final String TAG = "Notification";

    private String professeur;
    private String eleve;
    private String lieu;
    private String date;
    private String matiere;

    public Notification(String professeur, String eleve, String lieu, String date, String matiere) {
        this.professeur = professeur;
        this.eleve = eleve;
        this.lieu = lieu;
        this.date = date;
        this.matiere = matiere;
    }

    /** Méthode permettant de récupérer toutes les notifications de la base de données
     * @return List<Notification> : Liste des notifications
     * 
     * @see edu.iut.proftracker.models.Notification
     * @see edu.iut.proftracker.views.activitites.NotificationActivity
     * @see com.google.firebase.firestore.FirebaseFirestore
     * @see com.google.firebase.firestore.QueryDocumentSnapshot
     * @see com.google.firebase.firestore.QuerySnapshot
     * @see com.google.android.gms.tasks.Task
     */
    public static List<Notification> getNotification() {

        // Récupération de l'instance de la base de données Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Création d'une liste de notifications
        List<Notification> notifications = new ArrayList<>();

        // Récupération de toutes les notifications de la collection "notifications"
        db.collection("notifications")
                .get()
                .addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String professeur = document.getString("professeur");
                                String eleve = document.getString("eleve");
                                String lieu = document.getString("lieu");
                                String date = document.getString("date");
                                String matiere = document.getString("matiere");

                                Notification notification = new Notification(professeur, eleve, lieu, date, matiere);
                                notifications.add(notification);
                            }

                            for (Notification notification : notifications) {
                                System.out.println("getnotif de tout le monde");
                                System.out.println(notification.professeur + " " + notification.eleve + " " + notification.lieu + " " + notification.date + " " + notification.matiere);
                                Log.d(TAG, notification.professeur + " " + notification.eleve + " " + notification.lieu + " " + notification.date + " " + notification.matiere);
                            }
                        
                        } else {
                            Log.e(TAG, "Erreur lors de la récupération des notifications", task.getException());
                        }
                    });
    
        return notifications;
    }

    /** Méthode permettant de récupérer les notifications d'un professeur en particulier
     * 
     * @param profConcerne : Professeur concerné par les notifications
     * @param notificationActivity : Activité appelante
     * @return List<Notification> : Liste des notifications
     * 
     * @see edu.iut.proftracker.models.Notification
     * @see edu.iut.proftracker.views.activitites.NotificationActivity
     * @see com.google.firebase.firestore.FirebaseFirestore
     * @see com.google.firebase.firestore.QueryDocumentSnapshot
     * @see com.google.firebase.firestore.QuerySnapshot
     * @see com.google.android.gms.tasks.Task
     */

    public static List<Notification> getNotification(String profConcerne, NotificationActivity notificationActivity) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Notification> notifications = new ArrayList<>();

        db.collection("notifications")
                .whereEqualTo("professeur", profConcerne)
                .get()
                .addOnCompleteListener(
                    task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String professeur = document.getString("professeur");
                                String eleve = document.getString("eleve");
                                String lieu = document.getString("lieu");
                                String date = document.getString("date");
                                String matiere = document.getString("matiere");

                                Notification notification = new Notification(professeur, eleve, lieu, date, matiere);
                                notifications.add(notification);
                            }
                            for (Notification notification : notifications) {
                                System.out.println("getnotif de " + profConcerne);
                                System.out.println(notification.professeur + " " + notification.eleve + " " + notification.lieu + " " + notification.date + " " + notification.matiere);
                                Log.d(TAG, notification.professeur + " " + notification.eleve + " " + notification.lieu + " " + notification.date + " " + notification.matiere);
                            }
                            notificationActivity.onPostExecute(notifications);
                        } else {
                            Log.e(TAG, "Erreur lors de la récupération des notifications", task.getException());
                        }
                    });
        
        return notifications;
    }

    /** Méthode permettant de créer une notification dans la base de données
     * 
     * @param professeur : 
     * @param eleve
     * @param lieu
     * @param date
     * @param matiere
     */

    public static void createNotification(String professeur, String eleve, String lieu, String date, String matiere) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Notification notification = new Notification(professeur, eleve, lieu, date, matiere);

        db.collection("notifications")
                .add(notification)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Notification ajoutée avec succès");
                        } else {
                            Log.e(TAG, "Erreur lors de l'ajout de la notification", task.getException());
                        }
                    }
                });
    }

    public String getProfesseur() {
        return professeur;
    }

    public String getEleve() {
        return eleve;
    }

    public String getLieu() {
        return lieu;
    }

    public String getDate() {
        return date;
    }

    public String getMatiere() {
        return matiere;
    }
}
