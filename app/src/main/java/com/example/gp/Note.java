package com.example.gp;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Note {

    Date NoteTimes;
    String NoteTexts,NoteTitles, docId;

    public Note(){}

    public Note(Date noteTimes, String noteTexts, String noteTitles, String docId) {
        NoteTimes = noteTimes;
        NoteTexts = noteTexts;
        NoteTitles = noteTitles;
        this.docId = docId;
    }

    public Date getNoteTimes() {
        return NoteTimes;
    }

    public void setNoteTimes(Date noteTimes) {
        NoteTimes = noteTimes;
    }

    public String getNoteTexts() {
        return NoteTexts;
    }

    public void setNoteTexts(String noteTexts) {
        NoteTexts = noteTexts;
    }

    public String getNoteTitles() {
        return NoteTitles;
    }

    public void setNoteTitles(String noteTitles) {
        NoteTitles = noteTitles;
    }

    @DocumentId
    public String getDocId() {
        return docId;
    }
    @DocumentId
    public void setDocId(String docId) {
        this.docId = docId;
    }


}
