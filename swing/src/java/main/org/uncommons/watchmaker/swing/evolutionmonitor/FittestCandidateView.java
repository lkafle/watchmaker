// ============================================================================
//   Copyright 2006, 2007, 2008 Daniel W. Dyer
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
// ============================================================================
package org.uncommons.watchmaker.swing.evolutionmonitor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.interactive.Renderer;

/**
 * {@link EvolutionMonitor} view for displaying a graphical representation
 * of the fittest candidate found so far.  This allows us to monitor the
 * progress of an evolutionary algorithm.
 * @param <T> The type of the evolved entity displayed by this component.
 * @author Daniel Dyer
 */
class FittestCandidateView<T> extends JPanel implements EvolutionObserver<T>
{
    private static final Font BIG_FONT = new Font("Dialog", Font.BOLD, 16);

    private final Renderer<? super T, JComponent> renderer;
    private final JLabel fitnessLabel = new JLabel("N/A", JLabel.CENTER);
    private final JPanel view = new JPanel(new GridLayout(1, 1));

    /**
     * Creates a Swing view that uses the specified renderer to display
     * evolved entities.
     * @param renderer A renderer that convert evolved entities of the type
     * recognised by this view into Swing components.
     */
    public FittestCandidateView(Renderer<? super T, JComponent> renderer)
    {
        super(new BorderLayout(0, 10));
        this.renderer = renderer;

        JPanel header = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Fitness", JLabel.CENTER);
        header.add(label, BorderLayout.NORTH);
        fitnessLabel.setFont(BIG_FONT);
        header.add(fitnessLabel, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(view);
        
        // Set names for easier indentification in unit tests.
        fitnessLabel.setName("FitnessLabel");
        view.setName("ViewPanel");
    }

    
    public void populationUpdate(final PopulationData<? extends T> populationData)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                fitnessLabel.setText(String.valueOf(populationData.getBestCandidateFitness()));
                view.removeAll();
                view.add(renderer.render(populationData.getBestCandidate()));
            }
        });
    }
}
