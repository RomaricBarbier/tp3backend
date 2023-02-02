package comptoirs.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
 // Ce test est basé sur le jeu de données dans "test_data.sql"
class LigneServiceTest {
    static final int NUMERO_COMMANDE_DEJA_LIVREE = 99999;
    static final int NUMERO_COMMANDE_PAS_LIVREE  = 99998;
    static final int REFERENCE_PRODUIT_DISPONIBLE_1 = 93;
    static final int REFERENCE_PRODUIT_DISPONIBLE_2 = 94;
    static final int REFERENCE_PRODUIT_DISPONIBLE_3 = 95;
    static final int REFERENCE_PRODUIT_DISPONIBLE_4 = 96;
    static final int REFERENCE_PRODUIT_INDISPONIBLE = 97;
    static final int UNITES_COMMANDEES_AVANT = 0;

    @Autowired
    LigneService service;

    @Test
    void onPeutAjouterDesLignesSiPasLivre() {
        var ligne = service.ajouterLigne(NUMERO_COMMANDE_PAS_LIVREE, REFERENCE_PRODUIT_DISPONIBLE_1, 1);
        assertNotNull(ligne.getId(),
                "La ligne doit être enregistrée, sa clé générée");
    }

    @Test
    void laQuantiteEstPositive() {
            var ligne = service.ajouterLigne(NUMERO_COMMANDE_PAS_LIVREE, REFERENCE_PRODUIT_DISPONIBLE_1, 1);
            assertThrows(ConstraintViolationException.class,
                    () -> service.ajouterLigne(NUMERO_COMMANDE_PAS_LIVREE, REFERENCE_PRODUIT_DISPONIBLE_1, 0),
                        "Ligne doit être positive");
    }

    @Test
    void testLaCommandeDejaEnvoyee(){
        assertThrows(IllegalStateException.class, () -> service.ajouterLigne(99999,96,20), "La commande est déjà envoyée.");
    }
    @Test
    void testLaCommandeNExistePas(){

        assertThrows(Exception.class,
                () -> service.ajouterLigne(99, 98, 20));
        () -> service.ajouterLigne(99, 98, 20),"Commande inexistante?");
    }

    @Test
    void testQuandProduitNExistePas(){
        assertThrows(Exception.class,
                () -> service.ajouterLigne(99999, 4, 20));
    }
    @Test
    void testQuandLaCommandeDejaEnvoyee(){
        assertThrows(Exception.class,
                () -> service.ajouterLigne(99999, 98, 15));
    }

    @Test
    void testQuantiteEnStockSuffisante() {
        assertThrows(IllegalArgumentException.class,
                () -> service.ajouterLigne(99998, 98, 150));
    }
       @Test
        void testQuantiteEnStockPASSuffisante(){
            assertThrows(Exception.class,
                    () -> service.ajouterLigne(99998, 98, 290));
        }
}
