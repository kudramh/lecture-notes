package com.softtek.idtc.gemfire.vo;

import java.util.Date;
import com.gemstone.gemfire.pdx.PdxReader;
import com.gemstone.gemfire.pdx.PdxSerializable;
import com.gemstone.gemfire.pdx.PdxWriter;

public class SessionPdx implements PdxSerializable {

    private int id;
    private String  sessionName;
    private boolean lockStatus;
    private Date createdAt;
    private Date updatedAt;

    public SessionPdx() {
    }

    public SessionPdx(int id, String sessionName) {
        this.id = id;
        this.sessionName = sessionName;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.lockStatus = false;
    }

    @Override
    public void fromData(PdxReader reader) {
        id = reader.readInt("id");
        sessionName = reader.readString("sessionName");
        lockStatus = reader.readBoolean("lockStatus");
        createdAt = reader.readDate("createdAt");
        updatedAt = reader.readDate("updatedAt");
    }

    @Override
    public void toData(PdxWriter writer) {
        writer.writeInt("id", id)
              .markIdentityField("id") //identity field
              .writeString("sessionName", sessionName)
              .writeBoolean("lockStatus", lockStatus)
              .writeDate("createdAt", createdAt)
              .writeDate("updatedAt", updatedAt);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(String.format("Session [id=%d session_name=%s status=%s created_at=%s updated_at=%s]",
                    id, sessionName, lockStatus, createdAt, updatedAt)
                ).toString();
    }
}
