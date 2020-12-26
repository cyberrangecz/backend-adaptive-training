package com.example.demo.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;

@Entity
public class PhaseLevel extends BaseLevel {

    @OrderBy
    @OneToMany(mappedBy = "phaseLevel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GameLevel> subLevels;

    public List<GameLevel> getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(List<GameLevel> subLevels) {
        this.subLevels = subLevels;
    }
}
