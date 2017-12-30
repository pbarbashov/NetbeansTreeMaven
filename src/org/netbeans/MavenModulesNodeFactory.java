package org.netbeans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.modules.maven.NbMavenProjectImpl;
import org.netbeans.modules.maven.api.NbMavenProject;
import org.netbeans.modules.maven.spi.nodes.AbstractMavenNodeList;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;

import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

@NodeFactory.Registration(projectType = "org-netbeans-modules-maven", position = 400)
public class MavenModulesNodeFactory implements NodeFactory {

    @Override
    public NodeList<?> createNodes(Project prjct) {
        return new MavenModulesNodeList(prjct);
    }
    
    private class MavenModulesNodeList extends AbstractMavenNodeList<Project> implements PropertyChangeListener {
        private final NbMavenProjectImpl prjct;

        public MavenModulesNodeList(Project prjct) {
            this.prjct = prjct.getLookup().lookup(NbMavenProjectImpl.class);
        }

        @Override
        public List<Project> keys() {
            return new ArrayList<>(ProjectUtils.getContainedProjects(prjct, false));
        }

        @Override
        public Node node(final Project project) {
            Node node = project.getLookup().lookup(LogicalViewProvider.class).createLogicalView();
            return new FilterNode(node, new FilterNode.Children(node));
        }

        @Override
        public void addNotify() {
            NbMavenProject.addPropertyChangeListener(prjct, this);
        }

        @Override
        public void removeNotify() {
            NbMavenProject.removePropertyChangeListener(prjct, this);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            fireChange();        
        }    
    }
    
}
