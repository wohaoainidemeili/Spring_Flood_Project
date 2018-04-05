package yuan.flood.service.function;

import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.learning.BackPropagation;

public class TestListener implements LearningEventListener {
    @Override
    public void handleLearningEvent(LearningEvent event) {
        BackPropagation bp = (BackPropagation) event.getSource();
        if (event.getEventType() != LearningEvent.Type.LEARNING_STOPPED) {
            System.out.println(bp.getCurrentIteration() + ".iterotion:" + bp.getTotalNetworkError());
        }
    }
}
