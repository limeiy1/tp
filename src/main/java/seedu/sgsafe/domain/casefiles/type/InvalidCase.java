package seedu.sgsafe.domain.casefiles.type;

import seedu.sgsafe.domain.casefiles.Case;

public class InvalidCase extends Case {
    private final String invalidSaveString;

    public InvalidCase(String invalidSaveString) {
        super(null, null, null, null, null, null);
        this.invalidSaveString = invalidSaveString;
    }

    @Override
    public boolean isDeleted() {
        return true;
    }

    @Override
    public String toSaveString() {
        return this.invalidSaveString;
    }
}
