package prog.subject;

import prog.observer.Observer;
import prog.updateType.UpdateType;

public interface Subject {
    void registerObserver(Observer o);
    void unregisterObserver(Observer o);
    void notifyObservers(UpdateType t);
}
