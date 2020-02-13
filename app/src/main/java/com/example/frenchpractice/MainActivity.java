package com.example.frenchpractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

class verb {
    String[] myConjugations;
    String[] pronouns;
    String myName;
    String myExplanation;
    boolean tenseHint;
    verb(String name, String explanation) {
        myName = name;
        myExplanation = explanation;
        tenseHint = false;
    }
    String conjugate(int pronoun,int tense) {
        String phrase = "";
        phrase += pronouns[pronoun];
        phrase += " ";
        phrase += myConjugations[pronoun+tense*6];
        return phrase;
    }
}

class englishVerb extends verb {
    String[] myImperfectHelper;
    String ingForm;
    String futurePerfectForm;
    String simpleFutureForm;
    englishVerb (String name, String[] conjugations, String explanation, String[] imperfectHelper) {
        super(name, explanation);
        pronouns = new String[] {"I","You","He","We","You (formal)","They"};
        myImperfectHelper = imperfectHelper;
        ingForm = name + "ing";
        if (name.charAt(name.length()-1) == 'e') {
            ingForm = name.substring(0,name.length()-1) + "ing";
        }
        if (conjugations.length == 0) {
            myConjugations = new String[12];
            for (int i = 0; i < 2; i++) {
                myConjugations[i] = myName;
            }
            for (int i = 3; i < 6; i++) {
                myConjugations[i] = myName;
            }
            myConjugations[2] = myName+"s";
            if (name.charAt(name.length()-1)=='e') {
                for (int i = 6; i < 12; i++) {
                    myConjugations[i] = myName+"d";
                }
            } else {
                for (int i = 6; i < 12; i++) {
                    myConjugations[i] = myName+"ed";
                }
            }
        } else {
            myConjugations = conjugations;
        }
        futurePerfectForm = myConjugations[8];
        simpleFutureForm = myConjugations[0];
    }
    @Override
    String conjugate(int pronoun, int tense) {
        String phrase = "";
        if (tense == 0 || tense == 1) {//present and past
            phrase = super.conjugate(pronoun, tense);
        } else if (tense == 2) {//imperfect
            phrase = pronouns[pronoun] + " " + myImperfectHelper[pronoun+6] + " " + ingForm;
        } else if (tense == 3) {//simple future
            phrase = pronouns[pronoun] + " will " + simpleFutureForm;
        } else if (tense == 4) {//future perfect
            phrase = pronouns[pronoun] + " will have " + futurePerfectForm;
        } else if (tense == 5) {//near future
            phrase = pronouns[pronoun] + " " + myImperfectHelper[pronoun] + " going to " + myName;
        } else if (tense == 6) {//recent past
            phrase = pronouns[pronoun] + " just " + myConjugations[pronoun+6];
        } else if (tense == 7) {//conditional
            phrase = pronouns[pronoun] + " would " + simpleFutureForm;
        }
        return phrase;
    }
}

class esEnglishVerb extends englishVerb {
    esEnglishVerb (String name, String explanation, String[] imperfectHelper) {
        super(name, new String[0], explanation, imperfectHelper);
        myConjugations[2] = name+"es";
    }
}

class specialPast extends englishVerb {
    specialPast(String name, String past, String explanation, String[] imperfectHelper) {
        super(name, new String[0], explanation, imperfectHelper);
        for (int i = 6; i < 12; i++) {
            myConjugations[i] = past;
        }
        futurePerfectForm = myConjugations[8];
    }
}

class frenchVerb extends verb {
    String myPastParticiple;
    verb myPasseHelper;
    verb myTranslate;
    String myStem;
    String family;
    String imparfaitStem;
    String simpleFutureStem;
    String[] allerPresentConjugations;
    String[] venirPresentConjugations;
    frenchVerb (String name, verb translate, verb passeHelper, String explanation) {
        super(name, explanation);
        myStem = name.substring(0,name.length()-2);
        myTranslate = translate;
        myPasseHelper = passeHelper;
        pronouns = new String[] {"Je","Tu","Il","Nous","Vous","Ils"};
        simpleFutureStem = name;

        allerPresentConjugations = new String[6];
        allerPresentConjugations[0] = "vais";
        allerPresentConjugations[1] = "vas";
        allerPresentConjugations[2] = "va";
        allerPresentConjugations[3] = "allons";
        allerPresentConjugations[4] = "allez";
        allerPresentConjugations[5] = "vont";

        venirPresentConjugations = new String[6];
        venirPresentConjugations[0] = "viens";
        venirPresentConjugations[1] = "viens";
        venirPresentConjugations[2] = "vient";
        venirPresentConjugations[3] = "venons";
        venirPresentConjugations[4] = "venez";
        venirPresentConjugations[5] = "viennent";
    }
    String conjugate(int pronoun,int tense) {
        String phrase = "";
        if (tense == 0) {//present
            char checkContraction = myConjugations[pronoun].charAt(0);
            if (pronoun == 0 && (checkContraction == 'a' || checkContraction == 'e' || checkContraction == 'i' || checkContraction == 'o' || checkContraction == 'u' || checkContraction == 'h' || checkContraction == 'é' || checkContraction == 'è')) {
                phrase += "J'";
                phrase += myConjugations[pronoun];
            } else {
                phrase += pronouns[pronoun];
                phrase += " ";
                phrase += myConjugations[pronoun];
            }
        } else if (tense == 1) {//passé composé
            phrase += myPasseHelper.conjugate(pronoun, 0);
            phrase += " ";
            phrase += myPastParticiple;
            if (myPasseHelper.myName.equals("être")) {
                if (pronoun == 0 || pronoun == 3) {
                    phrase += "e";
                }
                if (pronoun == 3 || pronoun == 5) {
                    phrase += "s";
                }
            }
        } else if (tense == 2) {//imparfait
            char checkContraction = imparfaitStem.charAt(0);
            if (pronoun == 0 && (checkContraction == 'a' || checkContraction == 'e' || checkContraction == 'i' || checkContraction == 'o' || checkContraction == 'u' || checkContraction == 'h' || checkContraction == 'é' || checkContraction == 'è')) {
                phrase += "J'";
            } else {
                phrase += pronouns[pronoun];
                phrase += " ";
            }
            phrase += imparfaitStem;
            if (pronoun == 0 || pronoun == 1) {
                phrase += "ais";
            } else if (pronoun == 2) {
                phrase += "ait";
            } else if (pronoun == 3) {
                phrase += "ions";
            } else if (pronoun == 4) {
                phrase += "iez";
            } else if (pronoun == 5) {
                phrase += "aient";
            }
        } else if (tense == 3) {//simple future
            char checkContraction = simpleFutureStem.charAt(0);
            if (pronoun == 0 && (checkContraction == 'a' || checkContraction == 'e' || checkContraction == 'i' || checkContraction == 'o' || checkContraction == 'u' || checkContraction == 'h' || checkContraction == 'é' || checkContraction == 'è')) {
                phrase += "J'";
            } else {
                phrase += pronouns[pronoun];
                phrase += " ";
            }
            phrase += simpleFutureStem;
            if (pronoun == 0) {
                phrase += "ai";
            } else if (pronoun == 1) {
                phrase += "as";
            } else if (pronoun == 2) {
                phrase += "a";
            } else if (pronoun == 3) {
                phrase += "ons";
            } else if (pronoun == 4) {
                phrase += "ez";
            } else if (pronoun == 5) {
                phrase += "ont";
            }
        } else if (tense == 4) {//future perfect
            phrase += myPasseHelper.conjugate(pronoun, 3);
            phrase += " "+myPastParticiple;
        } else if (tense == 5) {//near future
            phrase += pronouns[pronoun];
            phrase += " "+allerPresentConjugations[pronoun];
            phrase += " "+myName;
        } else if (tense == 6) {//recent past
            phrase += pronouns[pronoun];
            phrase += " "+venirPresentConjugations[pronoun];
            phrase += " de "+myName;
        } else if (tense == 7) {//conditional
            char checkContraction = simpleFutureStem.charAt(0);
            if (pronoun == 0 && (checkContraction == 'a' || checkContraction == 'e' || checkContraction == 'i' || checkContraction == 'o' || checkContraction == 'u' || checkContraction == 'h' || checkContraction == 'é' || checkContraction == 'è')) {
                phrase += "J'";
            } else {
                phrase += pronouns[pronoun];
                phrase += " ";
            }
            phrase += simpleFutureStem;
            if (pronoun == 0 || pronoun == 1) {
                phrase += "ais";
            } else if (pronoun == 2) {
                phrase += "ait";
            } else if (pronoun == 3) {
                phrase += "ions";
            } else if (pronoun == 4) {
                phrase += "iez";
            } else if (pronoun == 5) {
                phrase += "aient";
            }
        }
        return phrase;
    }
}

class erVerb extends frenchVerb {
    //TODO: add accent rules
    erVerb (String name, verb translate,verb passeHelper, String explanation) {
        super(name,translate,passeHelper, explanation);
        family = "regular _er";
        myConjugations = new String[6];
        if (myStem.charAt(myStem.length()-1)=='y') {
            family = "stem-changing _er";
            myStem = myStem.substring(0, myStem.length()-1);
            myConjugations[0]=myStem+"ie";
            myConjugations[1]=myStem+"ies";
            myConjugations[2]=myStem+"ie";
            myConjugations[3]=myStem+"yons";
            myConjugations[4]=myStem+"yez";
            myConjugations[5]=myStem+"ient";
        } else if (myStem.charAt(myStem.length()-1)=='c') {
            family = "spelling-changing _er";
            myStem = myStem.substring(0, myStem.length()-1);
            myConjugations[0]=myStem+"ce";
            myConjugations[1]=myStem+"ces";
            myConjugations[2]=myStem+"ce";
            myConjugations[3]=myStem+"çons";
            myConjugations[4]=myStem+"çez";
            myConjugations[5]=myStem+"cent";
        } else if (myStem.charAt(myStem.length()-1)=='g') {
            family = "spelling-changing _er";
            myConjugations[0]=myStem+"e";
            myConjugations[1]=myStem+"es";
            myConjugations[2]=myStem+"e";
            myConjugations[3]=myStem+"eons";
            myConjugations[4]=myStem+"ez";
            myConjugations[5]=myStem+"ent";
        } else {
            myConjugations[0]=myStem+"e";
            myConjugations[1]=myStem+"es";
            myConjugations[2]=myStem+"e";
            myConjugations[3]=myStem+"ons";
            myConjugations[4]=myStem+"ez";
            myConjugations[5]=myStem+"ent";
        }
        myPastParticiple = myStem+"é";
        imparfaitStem = myConjugations[3].substring(0,myConjugations[3].length()-3);
    }
}

class reVerb extends frenchVerb {
    reVerb (String name, verb translate,verb passeHelper, String explanation) {
        super(name, translate, passeHelper, explanation);
        family = "regular _re";
        myConjugations = new String[6];
        myPastParticiple = myStem + "u";
        simpleFutureStem = name.substring(0, name.length() - 1);
        if (myStem.charAt(myStem.length() - 1) == 't' && myStem.charAt(myStem.length() - 2) == 't') {
            family = "mettre family _re";
            myStem = myStem.substring(0, myStem.length() - 1);
            myConjugations[0] = myStem + "s";
            myConjugations[1] = myStem + "s";
            myConjugations[2] = myStem + "";
            myConjugations[3] = myStem + "tons";
            myConjugations[4] = myStem + "tez";
            myConjugations[5] = myStem + "tent";
            if (myStem.charAt(myStem.length() - 2) == 'e' && myStem.charAt(myStem.length() - 3) == 'm') {
                myPastParticiple = myStem.substring(0, myStem.length() - 2) + "is";
            }
        } else if (myStem.charAt(myStem.length() - 1) == 't' && myStem.charAt(myStem.length() - 2) == 'î' && myStem.charAt(myStem.length() - 3) == 'a') {
            family = "connaître family _re";
            myStem = myStem.substring(0, myStem.length() - 3);
            myConjugations[0] = myStem + "ais";
            myConjugations[1] = myStem + "ais";
            myConjugations[2] = myStem + "aît";
            myConjugations[3] = myStem + "aissons";
            myConjugations[4] = myStem + "aissez";
            myConjugations[5] = myStem + "aissent";
            myPastParticiple = myStem + "u";
        } else if (myStem.length() >= 4 && myStem.charAt(myStem.length() - 1) == 'd' && myStem.charAt(myStem.length() - 2) == 'n' && myStem.charAt(myStem.length() - 3) == 'i' && myStem.charAt(myStem.length() - 4) == 'e') {
            family = "peindre family _re";
            myStem = myStem.substring(0, myStem.length() - 2);
            myConjugations[0] = myStem + "ns";
            myConjugations[1] = myStem + "ns";
            myConjugations[2] = myStem + "nt";
            myConjugations[3] = myStem + "gnons";
            myConjugations[4] = myStem + "gnez";
            myConjugations[5] = myStem + "gnent";
            myPastParticiple = myStem + "nt";
        } else {
            myConjugations[0]=myStem+"s";
            myConjugations[1]=myStem+"s";
            myConjugations[2]=myStem+"";
            myConjugations[3]=myStem+"ons";
            myConjugations[4]=myStem+"ez";
            myConjugations[5]=myStem+"ent";
        }
        imparfaitStem = myConjugations[3].substring(0,myConjugations[3].length()-3);
    }
}

class cuireFamily extends reVerb {
    cuireFamily (String name, verb translate,verb passeHelper, String explanation) {
        super(name,translate,passeHelper, explanation);
        family = "cuire family";
        myPastParticiple = myStem+"t";
        myConjugations[2] = myStem+"t";
        myConjugations[3]=myStem+"sons";
        myConjugations[4]=myStem+"sez";
        myConjugations[5]=myStem+"sent";
        imparfaitStem = myConjugations[3].substring(0,myConjugations[3].length()-3);
    }
}

class écrireFamily extends reVerb {
    écrireFamily (String name, verb translate,verb passeHelper, String explanation) {
        super(name,translate,passeHelper, explanation);
        family = "écrire family";
        myPastParticiple = myStem+"t";
        myConjugations[2] = myStem+"t";
        myConjugations[3] = myStem+"vons";
        myConjugations[4] = myStem+"vez";
        myConjugations[5] = myStem+"vent";
        imparfaitStem = myConjugations[3].substring(0,myConjugations[3].length()-3);
    }
}

class prendreFamily extends reVerb {
    prendreFamily (String name, verb translate,verb passeHelper, String explanation) {
        super(name,translate,passeHelper, explanation);
        family = "prendre family";
        myStem = myStem.substring(0, myStem.length()-1);
        myConjugations[3]=myStem+"ons";
        myConjugations[4]=myStem+"ez";
        myConjugations[5]=myStem+"nent";
        myStem = myStem.substring(0, myStem.length()-2);
        myPastParticiple = myStem+"is";
        imparfaitStem = myConjugations[3].substring(0,myConjugations[3].length()-3);
    }
}

class irVerb extends frenchVerb {
    irVerb (String name, verb translate, verb passeHelper, String explanation) {
        super(name, translate, passeHelper, explanation);
        family = "regular _ir";
        myConjugations = new String[6];
        if (myStem.charAt(myStem.length()-1)=='n' && myStem.charAt(myStem.length()-2)=='e') {
            family = "enir family";
            myStem = myStem.substring(0,myStem.length()-2);
            simpleFutureStem = myStem + "iendr";
            myPastParticiple = myStem+"enu";
            myConjugations[0] = myStem+"iens";
            myConjugations[1] = myStem+"iens";
            myConjugations[2] = myStem+"ient";
            myConjugations[3] = myStem+"enons";
            myConjugations[4] = myStem+"enez";
            myConjugations[5] = myStem+"iennent";
        } else {
            myPastParticiple = myStem+"i";
            myConjugations[0] = myStem+"is";
            myConjugations[1] = myStem+"is";
            myConjugations[2] = myStem+"it";
            myConjugations[3] = myStem+"issons";
            myConjugations[4] = myStem+"issez";
            myConjugations[5] = myStem+"issent";
        }
        imparfaitStem = myConjugations[3].substring(0,myConjugations[3].length()-3);
    }
}

class ouvrirFamily extends irVerb {
    ouvrirFamily (String name, verb translate,verb passeHelper, String explanation) {
        super(name,translate,passeHelper, explanation);
        family = "ouvrir family";
        myConjugations = new String[6];
        myConjugations[0]=myStem+"e";
        myConjugations[1]=myStem+"es";
        myConjugations[2]=myStem+"e";
        myConjugations[3]=myStem+"ons";
        myConjugations[4]=myStem+"ez";
        myConjugations[5]=myStem+"ent";
        myPastParticiple = myStem.substring(0,myStem.length()-1)+"ert";//Not correct for all verbs in this family
        imparfaitStem = myConjugations[3].substring(0,myConjugations[3].length()-3);
    }
}

class partirFamily extends irVerb {
    partirFamily (String name, verb translate, verb passeHelper, String explanation) {
        super(name, translate, passeHelper, explanation);
        family = "partir family";
        char lastChar = myStem.charAt(myStem.length()-1);
        myStem = myStem.substring(0,myStem.length()-1);
        myConjugations[0] = myStem+"s";
        myConjugations[1] = myStem+"s";
        myConjugations[2] = myStem+"t";
        myConjugations[3] = myStem+lastChar+"ons";
        myConjugations[4] = myStem+lastChar+"ez";
        myConjugations[5] = myStem+lastChar+"ent";
        imparfaitStem = myConjugations[3].substring(0,myConjugations[3].length()-3);
    }
}

public class MainActivity extends AppCompatActivity {
    //String test = "French!";
    String myInput;
    EditText answerEditText;
    TextView promptText;
    TextView OutputText;
    Button enterButton;
    ImageButton questionButton;

    //frenchVerb[] frenchVerbs;
    ArrayList<frenchVerb> frenchVerbs;
    int totalVerbs;
    ArrayList<frenchVerb> inUseVerbs;
    int totalInUseVerbs;
    ArrayList<frenchVerb> notInUseVerbs;
    int totalNotInUseVerbs;

    ArrayList<frenchVerb> fromCourse;
    int totalCourseVerbs;

    frenchVerb être;
    frenchVerb avoir;
    frenchVerb parler;
    frenchVerb marcher;
    frenchVerb tomber;
    frenchVerb manger;
    frenchVerb voir;
    frenchVerb répondre;
    frenchVerb prétendre;
    frenchVerb prendre;
    frenchVerb savoir;
    frenchVerb faire;
    frenchVerb combattre;
    frenchVerb admettre;
    frenchVerb descendre;
    frenchVerb debattre;
    frenchVerb attendre;
    frenchVerb finir;
    frenchVerb dormir;
    frenchVerb mentir;
    frenchVerb contradire;
    frenchVerb perdre;
    frenchVerb vendre;
    frenchVerb servir;
    frenchVerb conduire;
    frenchVerb prédire;
    frenchVerb lire;
    frenchVerb défendre;
    frenchVerb dire;
    frenchVerb mettre;
    frenchVerb promettre;
    frenchVerb surprendre;
    frenchVerb traduire;
    frenchVerb sortir;
    frenchVerb fondre;
    frenchVerb entendre;
    frenchVerb récrire;
    frenchVerb décrire;
    frenchVerb construire;
    frenchVerb écrire;
    frenchVerb comprendre;
    frenchVerb cuire;
    frenchVerb rendre;
    frenchVerb utiliser;
    frenchVerb apprendre;
    frenchVerb venir;
    frenchVerb tenir;
    frenchVerb revenir;
    frenchVerb provenir;
    frenchVerb obtenir;
    frenchVerb maintenir;
    frenchVerb devenir;
    frenchVerb contenir;
    frenchVerb appartenir;
    frenchVerb boire;
    frenchVerb connaître;
    frenchVerb apparaître;
    frenchVerb disparaître;
    frenchVerb reparaître;
    frenchVerb reconnaître;
    frenchVerb paraître;
    frenchVerb méconnaître;
    frenchVerb débattre;
    frenchVerb rester;
    frenchVerb passer;
    frenchVerb entrer;
    frenchVerb mourir;
    frenchVerb arriver;
    frenchVerb retourner;
    frenchVerb rentrer;
    frenchVerb monter;
    frenchVerb partir;
    frenchVerb vivre;
    frenchVerb survivre;
    frenchVerb aller;
    frenchVerb pouvoir;
    frenchVerb vouloir;
    frenchVerb devoir;
    frenchVerb envoyer;
    frenchVerb courir;
    frenchVerb habiter;
    frenchVerb cuisiner;
    frenchVerb chanter;
    frenchVerb dessiner;
    frenchVerb écouter;
    frenchVerb jardiner;
    frenchVerb peindre;
    frenchVerb jouer;
    frenchVerb louer;
    frenchVerb ouvrir;
    frenchVerb offrir;
    frenchVerb couvrir;
    frenchVerb souffrir;
    frenchVerb croire;
    frenchVerb recevoir;
    frenchVerb rire;
    frenchVerb sourire;
    frenchVerb suivre;
    frenchVerb déménager;
    frenchVerb emménager;

    englishVerb toBe;
    englishVerb toHave;
    englishVerb toGoOut;
    englishVerb toComeBack;
    englishVerb toComeFrom;
    englishVerb toBeUnawareOf;
    englishVerb toTalk;
    englishVerb toWalk;
    englishVerb toAnswer;
    englishVerb toClaim;
    englishVerb toDescend;
    englishVerb toWait;
    englishVerb toDebate;
    englishVerb toLie;
    englishVerb toContradict;
    englishVerb toServe;
    englishVerb toPredict;
    englishVerb toDefend;
    englishVerb toPromise;
    englishVerb toSurprise;
    englishVerb toTranslate;
    englishVerb toMelt;
    englishVerb toDescribe;
    englishVerb toCook;
    englishVerb toCook2;
    englishVerb toReturn;
    englishVerb toReturn2;
    englishVerb toReturn3;
    englishVerb toReturn4;
    englishVerb toUse;
    englishVerb toLearn;
    englishVerb toObtain;
    englishVerb toMaintain;
    englishVerb toContain;
    englishVerb toBelong;
    englishVerb toAppear;
    englishVerb toDisappear;
    englishVerb toReappear;
    englishVerb toRecognize;
    englishVerb toSeem;
    englishVerb toStay;
    englishVerb toEnter;
    englishVerb toDie;
    englishVerb toArrive;
    englishVerb toGoUp;
    englishVerb toLive;
    englishVerb toLive2;
    englishVerb toSurvive;
    englishVerb toWant;
    englishVerb toListen;
    englishVerb toGarden;
    englishVerb toPaint;
    englishVerb toPlay;
    englishVerb toRent;
    englishVerb toOpen;
    englishVerb toOffer;
    englishVerb toCover;
    englishVerb toSuffer;
    englishVerb toBelieve;
    englishVerb toReceive;
    englishVerb toLaugh;
    englishVerb toSmile;
    englishVerb toFollow;
    englishVerb toMoveIn;
    englishVerb toMoveOut;
    esEnglishVerb toFinish;
    esEnglishVerb toPass;
    specialPast toSleep;
    specialPast toFall;
    specialPast toEat;
    specialPast toSee;
    specialPast toTake;
    specialPast toKnow;
    specialPast toKnow2;
    specialPast toFight;
    specialPast toAdmit;
    specialPast toLose;
    specialPast toSell;
    specialPast toDrive;
    specialPast toRead;
    specialPast toSay;
    specialPast toPut;
    specialPast toHear;
    specialPast toRewrite;
    specialPast toBuild;
    specialPast toWrite;
    specialPast toUnderstand;
    specialPast toCome;
    specialPast toHold;
    specialPast toBecome;
    specialPast toDrink;
    specialPast toDo;
    specialPast toChoose;
    specialPast toLeave;
    specialPast toGo;
    specialPast toBeAbleTo;
    specialPast toHaveTo;
    specialPast toSend;
    specialPast toRun;
    specialPast toSing;
    specialPast toDraw;

    int language;
    int p;
    int t;
    String correct;
    String answer;
    byte[] bytes;
    int v;
    String hint;
    String prompt;

    int numCorrect = 0;
    int numIncorrect = 0;
    int numSkipped = 0;
    boolean answered = false;

    boolean doPresent;
    boolean doPast;
    boolean doImperfect;
    boolean doSimpleFuture;
    boolean doFuturePerfect;
    boolean doNearFuture;
    boolean doRecentPast;
    boolean doConditional;
    int numTenses;
    int[] tenses;

    boolean doAllVerbs;

    TextView correctText;
    TextView incorrectText;
    TextView skippedText;

    static String helpText;

    SharedPreferences prefs;
    SharedPreferences prefPrivate;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        promptText = (TextView) findViewById(R.id.prompt_text_id);
        answerEditText = (EditText) findViewById(R.id.answer_edit_text_id);
        OutputText = (TextView) findViewById(R.id.output_text_id);
        enterButton = (Button) findViewById(R.id.enter_button_id);
        questionButton = (ImageButton) findViewById(R.id.question_id);

        //numCorrect = 0;
        //numIncorrect = 0;
        //numSkipped = 0;

        correctText = (TextView) findViewById(R.id.correctNum);
        incorrectText = (TextView) findViewById(R.id.incorrectNum);
        skippedText = (TextView) findViewById(R.id.skippedNum);

        correctText.setText("Correct: 0");
        incorrectText.setText("Incorrect: 0");
        skippedText.setText("Skipped: 0");

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefPrivate = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = prefPrivate.edit();

        doAllVerbs = prefs.getBoolean("course_switch",true);
        
// then you use
        doPresent = prefs.getBoolean("present_switch", true);
        doPast = prefs.getBoolean("past_switch", true);
        doImperfect = prefs.getBoolean("imparfait_switch", true);
        doSimpleFuture = prefs.getBoolean("simple_future_switch", true);
        doFuturePerfect = prefs.getBoolean("future_perfect_switch",true);
        doNearFuture = prefs.getBoolean("near_future_switch",true);
        doRecentPast = prefs.getBoolean("recent_past_switch",true);
        doConditional = prefs.getBoolean("conditional_switch",true);
        /*if (!doPast && !doImperfect && !doSimpleFuture && !doFuturePerfect && !doNearFuture) {
            doPresent = true;
        }*/
        numTenses = 0;
        if (doPresent) {
            numTenses++;
        }
        if (doPast) {
            numTenses++;
        }
        if (doImperfect) {
            numTenses++;
        }
        if (doSimpleFuture) {
            numTenses++;
        }
        if (doFuturePerfect) {
            numTenses++;
        }
        if (doNearFuture) {
            numTenses++;
        }
        if (doRecentPast) {
            numTenses++;
        }
        if (doConditional) {
            numTenses++;
        }
        if (numTenses == 0) {
            doPresent = true;
            numTenses = 1;
        }
        tenses = new int[numTenses];
        int currentSlot = 0;
        if (doPresent) {
            tenses[currentSlot] = 0;
            currentSlot++;
        }
        if (doPast) {
            tenses[currentSlot] = 1;
            currentSlot++;
        }
        if (doImperfect) {
            tenses[currentSlot] = 2;
            currentSlot++;
        }
        if (doSimpleFuture) {
            tenses[currentSlot] = 3;
            currentSlot++;
        }
        if (doFuturePerfect) {
            tenses[currentSlot] = 4;
            currentSlot++;
        }
        if (doNearFuture) {
            tenses[currentSlot] = 5;
            currentSlot++;
        }
        if (doRecentPast) {
            tenses[currentSlot] = 6;
            currentSlot++;
        }
        if (doConditional) {
            tenses[currentSlot] = 7;
            currentSlot++;
        }

        //helloEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        String[] toBeC = new String[12];
        toBeC[0] = "am";
        toBeC[1] = "are";
        toBeC[2] = "is";
        toBeC[3] = "are";
        toBeC[4] = "are";
        toBeC[5] = "are";
        toBeC[6] = "was";
        toBeC[7] = "were";
        toBeC[8] = "was";
        toBeC[9] = "were";
        toBeC[10] = "were";
        toBeC[11] = "were";
        toBe = new englishVerb("be",toBeC,"",toBeC);
        toBe.ingForm = "being";
        toBe.futurePerfectForm = "been";
        toBe.simpleFutureForm = "be";

        String[] toHaveC = new String[12];
        toHaveC[0] = "have";
        toHaveC[1] = "have";
        toHaveC[2] = "has";
        toHaveC[3] = "have";
        toHaveC[4] = "have";
        toHaveC[5] = "have";
        for (int i = 6; i < 12; i++) {
            toHaveC[i] = "had";
        }
        toHave = new englishVerb("have",toHaveC,"",toBeC);
        toHave.futurePerfectForm = "had";
        //toHave.simpleFutureForm = "have";

        String[] toGoOutC = new String[12];
        toGoOutC[0] = "go out";
        toGoOutC[1] = "go out";
        toGoOutC[2] = "goes out";
        toGoOutC[3] = "go out";
        toGoOutC[4] = "go out";
        toGoOutC[5] = "go out";
        for (int i = 6; i < 12; i++) {
            toGoOutC[i] = "went out";
        }
        toGoOut = new englishVerb("go out",toGoOutC,"",toBeC);
        toGoOut.ingForm = "going out";
        toGoOut.futurePerfectForm = "went out";

        String[] toGoUpC = new String[12];
        toGoUpC[0] = "go up";
        toGoUpC[1] = "go up";
        toGoUpC[2] = "goes up";
        toGoUpC[3] = "go up";
        toGoUpC[4] = "go up";
        toGoUpC[5] = "go up";
        for (int i = 6; i < 12; i++) {
            toGoUpC[i] = "went up";
        }
        toGoUp = new englishVerb("go up",toGoUpC,"",toBeC);
        toGoUp.ingForm = "going up";
        toGoUp.futurePerfectForm = "went up";

        /*String[] toComeBackC = new String[12];
        toComeBackC[0] = "come back";
        toComeBackC[1] = "come back";
        toComeBackC[2] = "comes back";
        toComeBackC[3] = "come back";
        toComeBackC[4] = "come back";
        toComeBackC[5] = "come back";
        for (int i = 6; i < 12; i++) {
            toComeBackC[i] = "came back";
        }
        toComeBack = new englishVerb("come back",toComeBackC,"");*/

        String[] toComeFromC = new String[12];
        toComeFromC[0] = "come from";
        toComeFromC[1] = "come from";
        toComeFromC[2] = "comes from";
        toComeFromC[3] = "come from";
        toComeFromC[4] = "come from";
        toComeFromC[5] = "come from";
        for (int i = 6; i < 12; i++) {
            toComeFromC[i] = "came from";
        }
        toComeFrom = new englishVerb("come from",toComeFromC,"",toBeC);
        toComeFrom.ingForm = "coming from";
        toComeFrom.futurePerfectForm = "come from";

        String[] toBeUnawareOfC = new String[12];
        toBeUnawareOfC[0] = "am unaware of";
        toBeUnawareOfC[1] = "are unaware of";
        toBeUnawareOfC[2] = "is unaware of";
        toBeUnawareOfC[3] = "are unaware of";
        toBeUnawareOfC[4] = "are unaware of";
        toBeUnawareOfC[5] = "are unaware of";
        toBeUnawareOfC[6] = "was unaware of";
        toBeUnawareOfC[7] = "were unaware of";
        toBeUnawareOfC[8] = "was unaware of";
        toBeUnawareOfC[9] = "were unaware of";
        toBeUnawareOfC[10] = "were unaware of";
        toBeUnawareOfC[11] = "were unaware of";
        toBeUnawareOf = new englishVerb("unaware of",toBeUnawareOfC,"",toBeC);
        toBeUnawareOf.ingForm = "being unaware of";
        toBeUnawareOf.futurePerfectForm = "been unaware of";
        toBeUnawareOf.simpleFutureForm = "be unaware of";

        String[] toMoveInC = new String[12];
        toMoveInC[0] = "move in";
        toMoveInC[1] = "move in";
        toMoveInC[2] = "moves in";
        toMoveInC[3] = "move in";
        toMoveInC[4] = "move in";
        toMoveInC[5] = "move in";
        toMoveInC[6] = "moved in";
        toMoveInC[7] = "moved in";
        toMoveInC[8] = "moved in";
        toMoveInC[9] = "moved in";
        toMoveInC[10] = "moved in";
        toMoveInC[11] = "moved in";
        toMoveIn = new englishVerb("move in",toMoveInC,"",toBeC);
        toMoveIn.ingForm = "moving in";
        toMoveIn.futurePerfectForm = "moved in";
        toMoveIn.simpleFutureForm = "move in";

        String[] toMoveOutC = new String[12];
        toMoveOutC[0] = "move out";
        toMoveOutC[1] = "move out";
        toMoveOutC[2] = "moves out";
        toMoveOutC[3] = "move out";
        toMoveOutC[4] = "move out";
        toMoveOutC[5] = "move out";
        toMoveOutC[6] = "moved out";
        toMoveOutC[7] = "moved out";
        toMoveOutC[8] = "moved out";
        toMoveOutC[9] = "moved out";
        toMoveOutC[10] = "moved out";
        toMoveOutC[11] = "moved out";
        toMoveOut = new englishVerb("move out",toMoveOutC,"",toBeC);
        toMoveOut.ingForm = "moving out";
        toMoveOut.futurePerfectForm = "moved out";
        toMoveOut.simpleFutureForm = "move out";

        toTalk = new englishVerb("talk", new String[0],"",toBeC);
        toWalk = new englishVerb("walk", new String[0],"",toBeC);
        toAnswer = new englishVerb("answer",new String[0],"",toBeC);
        toClaim = new englishVerb("claim",new String[0],"",toBeC);
        toDescend = new englishVerb("descend",new String[0],"",toBeC);
        toWait = new englishVerb("wait",new String[0],"",toBeC);
        toDebate = new englishVerb("debate",new String[0],"",toBeC);
        toLie = new englishVerb("lie",new String[0],"",toBeC);
        toLie.ingForm = "lieing";
        toContradict = new englishVerb("contradict",new String[0],"",toBeC);
        toServe = new englishVerb("serve", new String[0],"",toBeC);
        toPredict = new englishVerb("predict", new String[0],"",toBeC);
        toDefend = new englishVerb("defend", new String[0],"",toBeC);
        toPromise = new englishVerb("promise", new String[0],"",toBeC);
        toSurprise = new englishVerb("surprise", new String[0],"",toBeC);
        toTranslate = new englishVerb("translate", new String[0],"",toBeC);
        toMelt = new englishVerb("melt", new String[0],"",toBeC);
        toDescribe = new englishVerb("describe", new String[0],"",toBeC);
        toCook = new englishVerb("cook", new String[0],"(e.g. the turkey cooks) ",toBeC);
        toCook2 = new englishVerb("cook", new String[0],"(e.g. I like to cook) ",toBeC);
        toReturn = new englishVerb("return", new String[0],"(i.e. to give something back) ",toBeC);
        toReturn2 = new englishVerb("return", new String[0],"(i.e. to go back to a location neither the speaker nor the subject is at currently.) ",toBeC);
        toReturn3 = new englishVerb("return", new String[0],"(i.e. to go back to a location where the speaker is, but the subject isn't.) ",toBeC);
        toReturn4 = new englishVerb("return", new String[0],"(i.e. to go back home) ",toBeC);
        toUse = new englishVerb("use",new String[0],"",toBeC);
        toLearn = new englishVerb("learn",new String[0],"",toBeC);
        toObtain = new englishVerb("obtain",new String[0],"",toBeC);
        toMaintain = new englishVerb("maintain",new String[0],"",toBeC);
        toContain = new englishVerb("contain",new String[0],"",toBeC);
        toBelong = new englishVerb("belong",new String[0],"",toBeC);
        toAppear = new englishVerb("appear",new String[0],"",toBeC);
        toDisappear = new englishVerb("disappear",new String[0],"",toBeC);
        toReappear = new englishVerb("reappear",new String[0],"",toBeC);
        toRecognize = new englishVerb("recognize",new String[0],"",toBeC);
        toSeem = new englishVerb("seem",new String[0],"",toBeC);
        toStay = new englishVerb("stay",new String[0],"",toBeC);
        toEnter = new englishVerb("enter",new String[0],"",toBeC);
        toDie = new englishVerb("die",new String[0],"",toBeC);
        toDie.ingForm = "dieing";
        toArrive = new englishVerb("arrive",new String[0],"",toBeC);
        toLive = new englishVerb("live",new String[0],"(to be alive)",toBeC);
        toLive2 = new englishVerb("live",new String[0],"(in a place)",toBeC);
        toWant = new englishVerb("want",new String[0],"",toBeC);
        toSurvive = new englishVerb("survive",new String[0],"",toBeC);
        toListen = new englishVerb("listen",new String[0],"",toBeC);
        toGarden = new englishVerb("garden",new String[0],"",toBeC);
        toPaint = new englishVerb("paint",new String[0],"",toBeC);
        toPlay = new englishVerb("play",new String[0],"",toBeC);
        toRent = new englishVerb("rent",new String[0],"",toBeC);
        toOpen = new englishVerb("open",new String[0],"",toBeC);
        toOffer = new englishVerb("offer",new String[0],"",toBeC);
        toCover = new englishVerb("cover",new String[0],"",toBeC);
        toSuffer = new englishVerb("suffer",new String[0],"",toBeC);
        toBelieve = new englishVerb("believe",new String[0], "",toBeC);
        toReceive = new englishVerb("receive",new String[0], "",toBeC);
        toLaugh = new englishVerb("laugh",new String[0], "",toBeC);
        toSmile = new englishVerb("smile",new String[0], "",toBeC);
        toFollow = new englishVerb("follow",new String[0], "",toBeC);

        toFinish = new esEnglishVerb("finish","",toBeC);
        toPass = new esEnglishVerb("pass","",toBeC);

        toChoose = new specialPast("choose","chose","",toBeC);
        toChoose.myConjugations[2]="chooses";
        toChoose.futurePerfectForm = "chosen";
        toDo = new specialPast("do", "did","",toBeC);
        toDo.myConjugations[2]="does";
        toDo.futurePerfectForm="chosen";
        toBeAbleTo = new specialPast("can","could","",toBeC);
        toBeAbleTo.myConjugations[2]="can";
        toBeAbleTo.ingForm = "being able to";
        toBeAbleTo.futurePerfectForm = "been able to";
        toBeAbleTo.simpleFutureForm = "be able to";
        toHaveTo = new specialPast("must","had to","",toBeC);
        toHaveTo.myConjugations[2] = "must";
        toHaveTo.ingForm = "having to";
        toHaveTo.simpleFutureForm = "have to";
        toRun = new specialPast("run","ran","",toBeC);
        toRun.futurePerfectForm = "run";
        toRun.ingForm = "running";

        toSleep = new specialPast("sleep", "slept","",toBeC);
        toFall = new specialPast("fall", "fell","",toBeC);
        toFall.futurePerfectForm = "fallen";
        toEat = new specialPast("eat", "ate","",toBeC);
        toEat.futurePerfectForm = "eaten";
        toSee = new specialPast("see", "saw","",toBeC);
        toSee.futurePerfectForm = "seen";
        toTake = new specialPast("take", "took","",toBeC);
        toTake.futurePerfectForm = "taken";
        toKnow = new specialPast("know", "knew","(an object, a skill)",toBeC);
        toKnow.futurePerfectForm = "known";
        toKnow2 = new specialPast("know", "knew","(a person)",toBeC);
        toKnow2.futurePerfectForm = "known";
        toFight = new specialPast("fight", "fought","",toBeC);
        toAdmit = new specialPast("admit", "admitted","",toBeC);
        toLose = new specialPast("lose","lost","",toBeC);
        toSell = new specialPast("sell","sold","",toBeC);
        toDrive = new specialPast("drive","drove","",toBeC);
        toDrive.futurePerfectForm = "driven";
        toRead = new specialPast("read","read","",toBeC);
        toRead.tenseHint = true;
        toSay = new specialPast("say","said","",toBeC);
        toPut = new specialPast("put","put","",toBeC);
        toPut.tenseHint = true;
        toPut.ingForm = "putting";
        toHear = new specialPast("hear","heard","",toBeC);
        toRewrite = new specialPast("rewrite","rewrote","",toBeC);
        toRewrite.futurePerfectForm = "rewritten";
        toBuild = new specialPast("build","built","",toBeC);
        toWrite = new specialPast("write","wrote","",toBeC);
        toWrite.futurePerfectForm = "written";
        toUnderstand = new specialPast("understand","understood","",toBeC);
        toCome = new specialPast("come","came","",toBeC);
        toCome.futurePerfectForm = "come";
        toHold = new specialPast("hold","held","",toBeC);
        toBecome = new specialPast("become","became","",toBeC);
        toBecome.futurePerfectForm = "become";
        toDrink = new specialPast("drink","drank","",toBeC);
        toLeave = new specialPast("leave","left","",toBeC);
        toGo = new specialPast("go","went","",toBeC);
        toSend = new specialPast("send","sent","",toBeC);
        toSing = new specialPast("sing","sang","",toBeC);
        toSing.futurePerfectForm = "sung";
        toDraw = new specialPast("draw","drew","",toBeC);
        toDraw.futurePerfectForm = "drawn";

        avoir = new frenchVerb("avoir",toHave,toHave,"");//toHave is temporary. We're initializing avoir right now, so we can't use it yet
        avoir.myPasseHelper = avoir;//correcting the passeHelper from toHave.
        avoir.myPastParticiple = "eu";
        String[] avoirC = new String[6];
        avoirC[0] = "ai";
        avoirC[1] = "as";
        avoirC[2] = "a";
        avoirC[3] = "avons";
        avoirC[4] = "avez";
        avoirC[5] = "ont";
        avoir.myConjugations = avoirC;
        avoir.family = "very irregular";
        avoir.imparfaitStem = "av";
        avoir.simpleFutureStem = "aur";

        être = new frenchVerb("être",toBe,avoir,"");
        être.myPastParticiple = "été";
        String[] êtreC = new String[6];
        êtreC[0] = "suis";
        êtreC[1] = "es";
        êtreC[2] = "est";
        êtreC[3] = "sommes";
        êtreC[4] = "êtes";
        êtreC[5] = "sont";
        être.myConjugations = êtreC;
        être.family = "very irregular";
        être.imparfaitStem = "ét";
        être.simpleFutureStem = "ser";

        voir = new frenchVerb("voir",toSee,avoir,"");
        voir.myPastParticiple = "vu";
        String[] voirC = new String[6];
        voirC[0] = "vois";
        voirC[1] = "vois";
        voirC[2] = "voit";
        voirC[3] = "voyons";
        voirC[4] = "voyez";
        voirC[5] = "voient";
        voir.myConjugations = voirC;
        voir.family = "very irregular";
        voir.imparfaitStem = voir.myConjugations[3].substring(0,voir.myConjugations[3].length()-3);
        voir.simpleFutureStem = "verr";

        pouvoir = new frenchVerb("pouvoir",toBeAbleTo,avoir,"");
        pouvoir.myPastParticiple = "pu";
        String[] pouvoirC = new String[6];
        pouvoirC[0] = "peux";
        pouvoirC[1] = "peux";
        pouvoirC[2] = "peut";
        pouvoirC[3] = "pouvons";
        pouvoirC[4] = "pouvez";
        pouvoirC[5] = "peuvent";
        pouvoir.myConjugations = pouvoirC;
        pouvoir.family = "very irregular";
        pouvoir.imparfaitStem = pouvoir.myConjugations[3].substring(0,pouvoir.myConjugations[3].length()-3);
        pouvoir.simpleFutureStem = "pourr";

        vouloir = new frenchVerb("vouloir",toWant,avoir,"");
        vouloir.myPastParticiple = "voulu";
        String[] vouloirC = new String[6];
        vouloirC[0] = "veux";
        vouloirC[1] = "veux";
        vouloirC[2] = "veut";
        vouloirC[3] = "voulons";
        vouloirC[4] = "voulez";
        vouloirC[5] = "veulent";
        vouloir.myConjugations = vouloirC;
        vouloir.family = "very irregular";
        vouloir.imparfaitStem = vouloir.myConjugations[3].substring(0,vouloir.myConjugations[3].length()-3);
        vouloir.simpleFutureStem = "voudr";

        savoir = new frenchVerb("savoir",toKnow,avoir,"");
        savoir.myPastParticiple = "su";
        String[] savoirC = new String[6];
        savoirC[0] = "sais";
        savoirC[1] = "sais";
        savoirC[2] = "sait";
        savoirC[3] = "savons";
        savoirC[4] = "savez";
        savoirC[5] = "savent";
        savoir.myConjugations = savoirC;
        savoir.family = "very irregular";
        savoir.imparfaitStem = savoir.myConjugations[3].substring(0,savoir.myConjugations[3].length()-3);
        savoir.simpleFutureStem = "saur";

        devoir = new frenchVerb("devoir",toHaveTo,avoir,"");
        devoir.myPastParticiple = "dû";
        String[] devoirC = new String[6];
        devoirC[0] = "dois";
        devoirC[1] = "dois";
        devoirC[2] = "doit";
        devoirC[3] = "devons";
        devoirC[4] = "devez";
        devoirC[5] = "doivent";
        devoir.myConjugations = devoirC;
        devoir.family = "very irregular";
        devoir.imparfaitStem = devoir.myConjugations[3].substring(0,devoir.myConjugations[3].length()-3);
        devoir.simpleFutureStem = "devr";

        recevoir = new frenchVerb("recevoir",toReceive,avoir,"");
        recevoir.myPastParticiple = "reçu";
        String[] recevoirC = new String[6];
        recevoirC[0] = "reçois";
        recevoirC[1] = "reçois";
        recevoirC[2] = "reçoit";
        recevoirC[3] = "recevons";
        recevoirC[4] = "recevez";
        recevoirC[5] = "reçoivent";
        recevoir.myConjugations = recevoirC;
        recevoir.family = "very irregular";
        recevoir.imparfaitStem = recevoir.myConjugations[3].substring(0,recevoir.myConjugations[3].length()-3);
        recevoir.simpleFutureStem = "recevr";

        faire = new frenchVerb("faire",toDo,avoir,"");
        faire.myPastParticiple = "fait";
        String[] faireC = new String[6];
        faireC[0] = "fais";
        faireC[1] = "fais";
        faireC[2] = "fait";
        faireC[3] = "faisons";
        faireC[4] = "faites";
        faireC[5] = "font";
        faire.myConjugations = faireC;
        faire.family = "very irregular";
        faire.imparfaitStem = faire.myConjugations[3].substring(0,faire.myConjugations[3].length()-3);
        faire.simpleFutureStem = "fer";

        boire = new reVerb("boire",toDrink,avoir,"");
        boire.myPastParticiple = "bu";
        String[] boireC = new String[6];
        boireC[0] = "bois";
        boireC[1] = "bois";
        boireC[2] = "boit";
        boireC[3] = "buvons";
        boireC[4] = "buvez";
        boireC[5] = "boivent";
        boire.myConjugations = boireC;
        boire.family = "very irregular";
        boire.imparfaitStem = boire.myConjugations[3].substring(0,boire.myConjugations[3].length()-3);

        croire = new reVerb("croire",toBelieve,avoir,"");
        croire.myPastParticiple = "cru";
        String[] croireC = new String[6];
        croireC[0] = "crois";
        croireC[1] = "crois";
        croireC[2] = "croit";
        croireC[3] = "croyons";
        croireC[4] = "croyez";
        croireC[5] = "croient";
        croire.myConjugations = croireC;
        croire.family = "very irregular";
        croire.imparfaitStem = croire.myConjugations[3].substring(0,croire.myConjugations[3].length()-3);

        rire = new reVerb("rire",toLaugh,avoir,"");
        rire.myPastParticiple = "ri";
        String[] rireC = new String[6];
        rireC[0] = "ris";
        rireC[1] = "ris";
        rireC[2] = "rit";
        rireC[3] = "rions";
        rireC[4] = "riez";
        rireC[5] = "rient";
        rire.myConjugations = rireC;
        rire.family = "very irregular";
        rire.imparfaitStem = rire.myConjugations[3].substring(0,rire.myConjugations[3].length()-3);

        sourire = new reVerb("sourire",toLaugh,avoir,"");
        sourire.myPastParticiple = "souri";
        String[] sourireC = new String[6];
        sourireC[0] = "souris";
        sourireC[1] = "souris";
        sourireC[2] = "sourit";
        sourireC[3] = "sourions";
        sourireC[4] = "souriez";
        sourireC[5] = "sourient";
        sourire.myConjugations = sourireC;
        sourire.family = "very irregular";
        sourire.imparfaitStem = sourire.myConjugations[3].substring(0,sourire.myConjugations[3].length()-3);

        mourir = new frenchVerb("mourir",toDie,être,"");
        mourir.myPastParticiple = "mort";
        String[] mourirC = new String[6];
        mourirC[0] = "meurs";
        mourirC[1] = "meurs";
        mourirC[2] = "meurt";
        mourirC[3] = "mourons";
        mourirC[4] = "mourez";
        mourirC[5] = "meurent";
        mourir.myConjugations = mourirC;
        mourir.family = "very irregular";
        mourir.imparfaitStem = mourir.myConjugations[3].substring(0,mourir.myConjugations[3].length()-3);

        vivre = new reVerb("vivre",toLive,avoir,"");
        vivre.myPastParticiple = "vécu";
        String[] vivreC = new String[6];
        vivreC[0] = "vis";
        vivreC[1] = "vis";
        vivreC[2] = "vit";
        vivreC[3] = "vivons";
        vivreC[4] = "vivez";
        vivreC[5] = "vivent";
        vivre.myConjugations = vivreC;
        vivre.family = "very irregular";
        vivre.imparfaitStem = vivre.myConjugations[3].substring(0,vivre.myConjugations[3].length()-3);

        survivre = new reVerb("survivre",toSurvive,avoir,"");
        survivre.myPastParticiple = "survécu";
        String[] survivreC = new String[6];
        survivreC[0] = "survis";
        survivreC[1] = "survis";
        survivreC[2] = "survit";
        survivreC[3] = "survivons";
        survivreC[4] = "survivez";
        survivreC[5] = "survivent";
        survivre.myConjugations = survivreC;
        survivre.family = "very irregular";
        survivre.imparfaitStem = survivre.myConjugations[3].substring(0,survivre.myConjugations[3].length()-3);

        suivre = new reVerb("suivre",toFollow,avoir,"");
        suivre.myPastParticiple = "suivi";
        String[] suivreC = new String[6];
        suivreC[0] = "suis";
        suivreC[1] = "suis";
        suivreC[2] = "suit";
        suivreC[3] = "suivons";
        suivreC[4] = "suivez";
        suivreC[5] = "suivent";
        suivre.myConjugations = suivreC;
        suivre.family = "very irregular";
        suivre.imparfaitStem = suivre.myConjugations[3].substring(0,suivre.myConjugations[3].length()-3);

        aller = new erVerb("aller",toGo,être,"");
        String[] allerC = new String[6];
        allerC[0] = "vais";
        allerC[1] = "vas";
        allerC[2] = "va";
        allerC[3] = "allons";
        allerC[4] = "allez";
        allerC[5] = "vont";
        aller.myConjugations = allerC;
        aller.family = "very irregular";
        aller.imparfaitStem = aller.myConjugations[3].substring(0,aller.myConjugations[3].length()-3);
        aller.simpleFutureStem = "ir";

        courir = new irVerb("courir",toRun,avoir,"");
        String[] courirC = new String[6];
        courirC[0] = "cours";
        courirC[1] = "cours";
        courirC[2] = "court";
        courirC[3] = "courons";
        courirC[4] = "courez";
        courirC[5] = "courent";
        courir.myConjugations = courirC;
        courir.family = "very irregular";
        courir.imparfaitStem = courir.myConjugations[3].substring(0,courir.myConjugations[3].length()-3);
        courir.myPastParticiple = "couru";
        courir.simpleFutureStem = "courr";

        parler = new erVerb("parler",toTalk,avoir,"");
        marcher = new erVerb("marcher",toWalk,avoir,"");
        tomber = new erVerb("tomber",toFall,être,"");
        manger = new erVerb("manger",toEat,avoir,"");
        utiliser = new erVerb("utiliser",toUse,avoir,"");
        rester = new erVerb("rester",toStay,être,"");
        passer = new erVerb("passer",toPass,être,"");
        entrer = new erVerb("entrer",toEnter,être,"");
        arriver = new erVerb("arriver",toArrive,être,"");
        retourner = new erVerb("retourner",toReturn2,être,"");
        rentrer = new erVerb("rentrer",toReturn4,être,"");
        monter = new erVerb("monter",toGoUp,être,"");
        envoyer = new erVerb("envoyer",toSend,avoir,"");
        envoyer.simpleFutureStem = "enverr";
        habiter = new erVerb("habiter",toLive2,avoir,"");
        cuisiner = new erVerb("cuisiner",toCook2,avoir,"");
        chanter = new erVerb("chanter",toSing,avoir,"");
        dessiner = new erVerb("dessiner",toDraw,avoir,"");
        écouter = new erVerb("écouter",toListen,avoir,"");
        jardiner = new erVerb("jardiner",toGarden,avoir,"");
        jouer = new erVerb("jouer",toPlay,avoir,"");
        louer = new erVerb("louer",toRent,avoir,"");
        emménager = new erVerb("emménager",toMoveIn,avoir,"");
        déménager = new erVerb("deménager",toMoveOut,avoir,"");

        répondre = new reVerb("répondre",toAnswer,avoir,"");
        prétendre = new reVerb("prétendre",toClaim,avoir,"");
        combattre = new reVerb("combattre",toFight,avoir,"");
        admettre = new reVerb("admettre",toAdmit,avoir,"");
        descendre = new reVerb("descendre",toDescend,être,"");
        débattre = new reVerb("débattre",toDebate,avoir,"");
        attendre = new reVerb("attendre",toWait,avoir,"");
        perdre = new reVerb("perdre",toLose,avoir,"");
        vendre = new reVerb("vendre",toSell,avoir,"");
        défendre = new reVerb("défendre",toDefend,avoir,"");
        mettre = new reVerb("mettre",toPut,avoir,"");
        promettre = new reVerb("promettre",toPromise,avoir,"");
        fondre = new reVerb("fondre",toMelt,avoir,"");
        entendre = new reVerb("entendre",toHear,avoir,"");
        rendre = new reVerb("rendre",toReturn,avoir,"");

        peindre = new reVerb("peindre",toPaint,avoir,"");

        connaître = new reVerb("connaître",toKnow2,avoir,"");
        apparaître = new reVerb("apparaître",toAppear,avoir,"");
        disparaître = new reVerb("disparaître",toDisappear,avoir,"");
        reparaître = new reVerb("reparaître",toReappear,avoir,"");
        reconnaître = new reVerb("reconnaître",toRecognize,avoir,"");
        paraître = new reVerb("paraître",toSeem,avoir,"");
        méconnaître = new reVerb("méconnaître",toBeUnawareOf,avoir,"");

        contradire = new cuireFamily("contradire",toContradict,avoir,"");
        conduire = new cuireFamily("conduire",toDrive,avoir,"");
        prédire = new cuireFamily("prédire",toPredict,avoir,"");
        lire = new cuireFamily("lire",toRead,avoir,"");
        traduire = new cuireFamily("traduire",toTranslate,avoir,"");
        construire = new cuireFamily("construire",toBuild,avoir,"");
        cuire = new cuireFamily("cuire",toCook,avoir,"");

        dire = new cuireFamily("dire",toSay,avoir,"");
        dire.myConjugations[4] = "dites";
        dire.family = "dire family";

        récrire = new écrireFamily("récrire",toRewrite,avoir,"");
        décrire = new écrireFamily("décrire",toDescribe,avoir,"");
        écrire = new écrireFamily("écrire",toWrite,avoir,"");

        prendre = new prendreFamily("prendre",toTake,avoir,"");
        surprendre = new prendreFamily("surprendre",toSurprise,avoir,"");
        comprendre = new prendreFamily("comprendre",toUnderstand,avoir,"");
        apprendre = new prendreFamily("apprendre",toLearn,avoir,"");

        finir = new irVerb("finir",toFinish,avoir,"");

        venir = new irVerb("venir",toCome,être,"");
        revenir = new irVerb("revenir",toReturn3,être,"");
        provenir = new irVerb("provenir",toComeFrom,avoir,"");
        devenir = new irVerb("devenir",toBecome,être,"");
        tenir = new irVerb("tenir",toHold,avoir,"");
        obtenir = new irVerb("obtenir",toObtain,avoir,"");
        maintenir = new irVerb("maintenir",toMaintain,avoir,"");
        contenir = new irVerb("contenir",toContain,avoir,"");
        appartenir = new irVerb("appartenir",toBelong,avoir,"");

        dormir = new partirFamily("dormir",toSleep,avoir,"");
        mentir = new partirFamily("mentir",toLie,avoir,"");
        servir = new partirFamily("servir",toServe,avoir,"");
        sortir = new partirFamily("sortir",toGoOut,être,"");
        partir = new partirFamily("partir",toLeave,être,"");

        ouvrir = new ouvrirFamily("ouvrir",toOpen,avoir,"");
        couvrir = new ouvrirFamily("couvrir",toCover,avoir,"");
        offrir = new ouvrirFamily("offrir",toOffer,avoir,"");
        souffrir = new ouvrirFamily("souffrir",toSuffer,avoir,"");

        //frenchVerbs = new frenchVerb[73];
        frenchVerbs = new ArrayList<frenchVerb>();
        frenchVerbs.add(être);
        frenchVerbs.add(avoir);
        frenchVerbs.add(parler);
        frenchVerbs.add(marcher);
        frenchVerbs.add(tomber);
        frenchVerbs.add(manger);
        frenchVerbs.add(voir);
        frenchVerbs.add(répondre);
        frenchVerbs.add(prétendre);
        frenchVerbs.add(prendre);
        frenchVerbs.add(savoir);
        frenchVerbs.add(faire);
        frenchVerbs.add(combattre);
        frenchVerbs.add(admettre);
        frenchVerbs.add(descendre);
        frenchVerbs.add(débattre);
        frenchVerbs.add(attendre);
        frenchVerbs.add(finir);
        frenchVerbs.add(dormir);
        frenchVerbs.add(mentir);
        frenchVerbs.add(contradire);
        frenchVerbs.add(perdre);
        frenchVerbs.add(vendre);
        frenchVerbs.add(servir);
        frenchVerbs.add(conduire);
        frenchVerbs.add(prédire);
        frenchVerbs.add(lire);
        frenchVerbs.add(défendre);
        frenchVerbs.add(dire);
        frenchVerbs.add(mettre);
        frenchVerbs.add(promettre);
        frenchVerbs.add(surprendre);
        frenchVerbs.add(traduire);
        frenchVerbs.add(sortir);
        frenchVerbs.add(fondre);
        frenchVerbs.add(entendre);
        frenchVerbs.add(récrire);
        frenchVerbs.add(décrire);
        frenchVerbs.add(construire);
        frenchVerbs.add(écrire);
        frenchVerbs.add(comprendre);
        frenchVerbs.add(cuire);
        frenchVerbs.add(rendre);
        frenchVerbs.add(utiliser);
        frenchVerbs.add(apprendre);
        frenchVerbs.add(venir);
        frenchVerbs.add(tenir);
        frenchVerbs.add(revenir);
        frenchVerbs.add(provenir);
        frenchVerbs.add(obtenir);
        frenchVerbs.add(maintenir);
        frenchVerbs.add(devenir);
        frenchVerbs.add(contenir);
        frenchVerbs.add(appartenir);
        frenchVerbs.add(boire);
        frenchVerbs.add(connaître);
        frenchVerbs.add(apparaître);
        frenchVerbs.add(disparaître);
        frenchVerbs.add(reparaître);
        frenchVerbs.add(reconnaître);
        frenchVerbs.add(paraître);
        frenchVerbs.add(méconnaître);
        frenchVerbs.add(rester);
        frenchVerbs.add(passer);
        frenchVerbs.add(entrer);
        frenchVerbs.add(mourir);
        frenchVerbs.add(arriver);
        frenchVerbs.add(retourner);
        frenchVerbs.add(rentrer);
        frenchVerbs.add(monter);
        frenchVerbs.add(partir);
        frenchVerbs.add(vivre);
        frenchVerbs.add(survivre);
        frenchVerbs.add(pouvoir);
        frenchVerbs.add(devoir);
        frenchVerbs.add(aller);
        frenchVerbs.add(vouloir);
        frenchVerbs.add(envoyer);
        frenchVerbs.add(courir);
        frenchVerbs.add(habiter);
        frenchVerbs.add(cuisiner);
        frenchVerbs.add(chanter);
        frenchVerbs.add(dessiner);
        frenchVerbs.add(écouter);
        frenchVerbs.add(jardiner);
        frenchVerbs.add(peindre);
        frenchVerbs.add(jouer);
        frenchVerbs.add(ouvrir);
        frenchVerbs.add(croire);
        frenchVerbs.add(recevoir);
        frenchVerbs.add(rire);
        frenchVerbs.add(sourire);
        frenchVerbs.add(suivre);
        frenchVerbs.add(louer);
        frenchVerbs.add(emménager);
        frenchVerbs.add(déménager);
        frenchVerbs.add(courir);
        frenchVerbs.add(souffrir);
        frenchVerbs.add(offrir);

        fromCourse = new ArrayList<frenchVerb>();
        fromCourse.add(être);
        fromCourse.add(avoir);
        fromCourse.add(habiter);
        fromCourse.add(vivre);
        fromCourse.add(cuisiner);
        fromCourse.add(lire);
        fromCourse.add(chanter);
        fromCourse.add(dessiner);
        fromCourse.add(écouter);
        fromCourse.add(jardiner);
        fromCourse.add(peindre);
        fromCourse.add(jouer);
        fromCourse.add(vendre);
        fromCourse.add(finir);
        fromCourse.add(boire);
        fromCourse.add(courir);
        fromCourse.add(dire);
        fromCourse.add(ouvrir);
        fromCourse.add(pouvoir);
        fromCourse.add(savoir);
        fromCourse.add(voir);
        fromCourse.add(conduire);
        fromCourse.add(croire);
        fromCourse.add(dormir);
        //fromCourse.add(recevoir);
        fromCourse.add(tenir);
        fromCourse.add(vouloir);
        fromCourse.add(faire);
        fromCourse.add(connaître);
        fromCourse.add(devoir);
        fromCourse.add(écrire);
        fromCourse.add(mettre);
        fromCourse.add(prendre);
        fromCourse.add(comprendre);
        fromCourse.add(apprendre);
        fromCourse.add(rire);
        fromCourse.add(suivre);
        fromCourse.add(louer);
        fromCourse.add(emménager);
        fromCourse.add(déménager);
        fromCourse.add(aller);
        fromCourse.add(entrer);
        fromCourse.add(arriver);
        fromCourse.add(descendre);
        fromCourse.add(rester);
        fromCourse.add(tomber);
        fromCourse.add(retourner);
        fromCourse.add(rentrer);
        fromCourse.add(revenir);
        fromCourse.add(venir);
        fromCourse.add(sortir);
        fromCourse.add(partir);
        fromCourse.add(monter);
        fromCourse.add(mourir);

        totalVerbs = frenchVerbs.size();
        totalInUseVerbs = frenchVerbs.size();
        totalCourseVerbs = fromCourse.size();
        totalNotInUseVerbs = 0;

        inUseVerbs = new ArrayList<frenchVerb>();
        notInUseVerbs = new ArrayList<frenchVerb>();
        for (int i = 0; i < totalVerbs; i++) {
            inUseVerbs.add(frenchVerbs.get(i));
        }

        promptText.setText(" ");

        if (doAllVerbs) {
            v = (int) (frenchVerbs.size()*Math.random());
        } else {
            v = (int) (fromCourse.size()*Math.random());
        }
        language = (int) (2*Math.random());
        p = (int) (6*Math.random());
        t = (int) (numTenses*Math.random());
        t = tenses[t];
        hint = new String("");
        prompt = "";

        if (doAllVerbs) {
            if (language == 0) {//translate French to English
                //System.out.println(frenchVerbs.get(v).conjugate(p, t));
                prompt += frenchVerbs.get(v).conjugate(p, t);
                hint += frenchVerbs.get(v).myExplanation;
                correct = frenchVerbs.get(v).myTranslate.conjugate(p, t);
            } else {//translate English to French
                prompt += frenchVerbs.get(v).myTranslate.conjugate(p, t);
                hint += frenchVerbs.get(v).myTranslate.myExplanation;
                correct = frenchVerbs.get(v).conjugate(p, t);
            }
            //Some English verbs are conjugation the same way in the past as the present. To clear up any ambiguity, we say what tense we're looking for.
            if (language == 1 && frenchVerbs.get(v).myTranslate.tenseHint) {
                if (t == 0) {
                    hint += "(present)";
                } else if (t == 1) {
                    hint += "(past)";
                } else if (t == 2) {
                    hint += "(imperfect)";
                } else if (t == 3) {
                    hint += "(simple future)";
                }
            }
            prompt = prompt + " \n" +hint;
            promptText.setText(prompt);
        } else {
            if (language == 0) {//translate French to English
                //System.out.println(fromCourse.get(v).conjugate(p, t));
                prompt += fromCourse.get(v).conjugate(p, t);
                hint += fromCourse.get(v).myExplanation;
                correct = fromCourse.get(v).myTranslate.conjugate(p, t);
            } else {//translate English to French
                prompt += fromCourse.get(v).myTranslate.conjugate(p, t);
                hint += fromCourse.get(v).myTranslate.myExplanation;
                correct = fromCourse.get(v).conjugate(p, t);
            }
            //Some English verbs are conjugation the same way in the past as the present. To clear up any ambiguity, we say what tense we're looking for.
            if (language == 1 && fromCourse.get(v).myTranslate.tenseHint) {
                if (t == 0) {
                    hint += "(present)";
                } else if (t == 1) {
                    hint += "(past)";
                } else if (t == 2) {
                    hint += "(imperfect)";
                } else if (t == 3) {
                    hint += "(simple future)";
                }
            }
            prompt = prompt + " \n" +hint;
            promptText.setText(prompt);
        }

        if (prefPrivate.getInt("correct",-1) != -1) {
            numCorrect = prefPrivate.getInt("correct",0);
            numIncorrect = prefPrivate.getInt("incorrect",0);
            numSkipped = prefPrivate.getInt("skipped",0);
            correctText.setText("Correct: "+numCorrect);
            incorrectText.setText("Incorrect: "+numIncorrect);
            skippedText.setText("Skipped: "+numSkipped);
            prompt = prefPrivate.getString("prompt","");
            promptText.setText(prompt);
            OutputText.setText(prefPrivate.getString("output",""));
            correct = prefPrivate.getString("answer","");
            answered = prefPrivate.getBoolean("answered",false);
            v = prefPrivate.getInt("v",0);
        }

        /*if (savedInstanceState != null) {
            numCorrect = savedInstanceState.getInt("correct");
            numIncorrect = savedInstanceState.getInt("incorrect");
            numSkipped = savedInstanceState.getInt("skipped");
            correctText.setText("Correct: "+numCorrect);
            incorrectText.setText("Incorrect: "+numIncorrect);
            skippedText.setText("Skipped: "+numSkipped);

            prompt = savedInstanceState.getString("prompt");
            promptText.setText(prompt);
            OutputText.setText(savedInstanceState.getString("output"));
            answered = savedInstanceState.getBoolean("answered");
            v = savedInstanceState.getInt("v",0);
        }*/
    }

    //Called when enter button is pressed
    public void enterText(View view) {
        answer=answerEditText.getText().toString();
        answer = answer.toLowerCase();//I don't want capital letters to matter:
        if (answer.equals(correct.toLowerCase())) {
            OutputText.setText("Correct!");
            numCorrect++;
            answered = true;
        } else {
            OutputText.setText("That's not right. The correct answer was "+correct);
            numIncorrect++;
            answered = true;
        }
    }

    public void nextPrompt(View view) {
        if (doAllVerbs) {
            v = (int) (frenchVerbs.size()*Math.random());
        } else {
            v = (int) (fromCourse.size()*Math.random());
        }
        language = (int) (2*Math.random());
        p = (int) (6*Math.random());
        t = (int) (numTenses*Math.random());
        t = tenses[t];
        hint = new String("");
        prompt = "";
        
        if (doAllVerbs) {
            if (language == 0) {//translate French to English
                //System.out.println(frenchVerbs.get(v).conjugate(p, t));
                prompt += frenchVerbs.get(v).conjugate(p, t);
                hint += frenchVerbs.get(v).myExplanation;
                correct = frenchVerbs.get(v).myTranslate.conjugate(p, t);
            } else {//translate English to French
                prompt += frenchVerbs.get(v).myTranslate.conjugate(p, t);
                hint += frenchVerbs.get(v).myTranslate.myExplanation;
                correct = frenchVerbs.get(v).conjugate(p, t);
            }
            //Some English verbs are conjugation the same way in the past as the present. To clear up any ambiguity, we say what tense we're looking for.
            if (language == 1 && frenchVerbs.get(v).myTranslate.tenseHint) {
                if (t == 0) {
                    hint += "(present)";
                } else if (t == 1) {
                    hint += "(past)";
                } else if (t == 2) {
                    hint += "(imperfect)";
                } else if (t == 3) {
                    hint += "(simple future)";
                }
            }
        } else {
            if (language == 0) {//translate French to English
                //System.out.println(fromCourse.get(v).conjugate(p, t));
                prompt += fromCourse.get(v).conjugate(p, t);
                hint += fromCourse.get(v).myExplanation;
                correct = fromCourse.get(v).myTranslate.conjugate(p, t);
            } else {//translate English to French
                prompt += fromCourse.get(v).myTranslate.conjugate(p, t);
                hint += fromCourse.get(v).myTranslate.myExplanation;
                correct = fromCourse.get(v).conjugate(p, t);
            }
            //Some English verbs are conjugation the same way in the past as the present. To clear up any ambiguity, we say what tense we're looking for.
            if (language == 1 && fromCourse.get(v).myTranslate.tenseHint) {
                if (t == 0) {
                    hint += "(present)";
                } else if (t == 1) {
                    hint += "(past)";
                } else if (t == 2) {
                    hint += "(imperfect)";
                } else if (t == 3) {
                    hint += "(simple future)";
                }
            }
        }

        if (!answered) {
            numSkipped++;
        }

        correctText.setText("Correct: "+numCorrect);
        incorrectText.setText("Incorrect: "+numIncorrect);
        skippedText.setText("Skipped: "+numSkipped);

        prompt = prompt + " \n" +hint;
        promptText.setText(prompt);
        OutputText.setText("");
        answerEditText.setText("");
        answered = false;
    }

    public void help (View view) {
        frenchVerb current;
        if (doAllVerbs) {
            current = frenchVerbs.get(v);
        } else {
            current = fromCourse.get(v);
        }
        helpText = current.myName+" is a "+current.family+" verb.\nIts translation is to "+current.myTranslate.myName+".\n";
        if (current.myTranslate.myExplanation != null) {
            helpText += current.myTranslate.myExplanation+"\n";
        }
        helpText += "\n";
        for (int i = 0; i < 6; i++) {
            helpText += current.conjugate(i,0);
            if (i % 2 == 1 && i != 5) {
                helpText += "\n";
            } else {
                helpText += "   ";
            }
        }
        helpText += "\n\nIts past participle is "+current.myPastParticiple+", and it uses "+current.myPasseHelper.myName+" for passé composé and future perfect.";
        helpText += "\nIts nous conjugation is "+current.myConjugations[3]+", so its imparfait stem is "+current.imparfaitStem+".";
        if (current.myName == "être") {
            helpText += " (être is weird)";
        }
        helpText += "\nIts simple future stem is "+ current.simpleFutureStem+".";
        Intent i = new Intent(getApplicationContext(),PopActivity.class);
        startActivity(i);
    }

    public void clear (View view) {
        editor.putInt("correct", 0);
        editor.putInt("incorrect", 0);
        editor.putInt("skipped", 0);
        editor.commit();
        numCorrect = 0;
        numSkipped = 0;
        numIncorrect = 0;
        correctText.setText("Correct: "+numCorrect);
        incorrectText.setText("Incorrect: "+numIncorrect);
        skippedText.setText("Skipped: "+numSkipped);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_id:
                editor.putString("prompt",prompt);
                editor.putString("output", OutputText.getText().toString());
                editor.putString("answer", correct);
                editor.putBoolean("answered",answered);
                editor.putInt("v",v);
                editor.putInt("correct", numCorrect);
                editor.putInt("incorrect", numIncorrect);
                editor.putInt("skipped", numSkipped);
                editor.commit();
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            /*case R.id.manage_verbs_id:
                editor.putString("prompt",prompt);
                editor.putString("output", OutputText.getText().toString());
                editor.putString("answer", correct);
                editor.putBoolean("answered",answered);
                editor.putInt("v",v);
                editor.putInt("correct", numCorrect);
                editor.putInt("incorrect", numIncorrect);
                editor.putInt("skipped", numSkipped);
                editor.commit();
                //Intent manageVerbsIntent = new Intent(this, SettingsActivity.class);
                //startActivity(manageVerbsIntent);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        editor.putString("prompt",prompt);
        editor.putString("output", OutputText.getText().toString());
        editor.putString("answer", correct);
        editor.putBoolean("answered",answered);
        editor.putInt("correct", numCorrect);
        editor.putInt("incorrect", numIncorrect);
        editor.putInt("skipped", numSkipped);
        editor.putInt("v",v);
        editor.commit();
        /*outState.putInt("correct", numCorrect);
        outState.putInt("incorrect", numIncorrect);
        outState.putInt("skipped", numSkipped);
        outState.putString("prompt",prompt);
        outState.putString("output", OutputText.getText().toString());
        outState.putBoolean("answered",answered);
        outState.putInt("v",v);*/
    }
}
