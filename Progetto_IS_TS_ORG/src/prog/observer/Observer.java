package prog.observer;

import prog.subject.Subject;
import prog.updateType.UpdateType;

public interface Observer {
    void update(Subject s, UpdateType t);
}
