package org.jaguar.modules.workflow.controller;

import com.yworks.yfiles.server.graphml.folding.FoldedLayoutGraph;
import com.yworks.yfiles.server.graphml.support.GraphRoundtripSupport;
import y.layout.LayoutOrientation;
import y.layout.Layouter;
import y.layout.hierarchic.IncrementalHierarchicLayouter;
import y.layout.orthogonal.OrthogonalGroupLayouter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lvws
 * @since 2019/4/16.
 */
@WebServlet(urlPatterns = "/flow_definition/graph_layout")
public class GraphLayoutServlet extends HttpServlet {

    private static final String GRID = "grid";
    private static final String HIERARCHICORTH = "hierarchicOrth";

    /**
     * 流程布局
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GraphRoundtripSupport support = new GraphRoundtripSupport();
        FoldedLayoutGraph graph = (FoldedLayoutGraph) support.createRoundtripGraph();
        support.readGraph(request, graph);

        String type = request.getParameter("type");
        String minimumLayerDistance = request.getParameter("minimumLayerDistance");
        String nodeToEdgeDistance = request.getParameter("nodeToEdgeDistance");
        String gridGap = request.getParameter("gridGap");

        Layouter layouter;
        if (GRID.equals(type)) {
            OrthogonalGroupLayouter orthogonalGroupLayouter = new OrthogonalGroupLayouter();
            orthogonalGroupLayouter.setLayoutOrientation(LayoutOrientation.RIGHT_TO_LEFT);
            orthogonalGroupLayouter.setConsiderNodeLabelsEnabled(true);
            orthogonalGroupLayouter.setGrid(Integer.parseInt(gridGap));
            layouter = orthogonalGroupLayouter;
        } else {
            IncrementalHierarchicLayouter ihl = new IncrementalHierarchicLayouter();
            //控制布局方向
            ihl.setLayoutOrientation(LayoutOrientation.LEFT_TO_RIGHT);
            ihl.setConsiderNodeLabelsEnabled(true);

            ihl.setOrthogonallyRouted(HIERARCHICORTH.equals(type));

            //没层之间的最小距离
            ihl.setMinimumLayerDistance(Double.parseDouble(minimumLayerDistance));
            ihl.setNodeToEdgeDistance(Double.parseDouble(nodeToEdgeDistance));
            layouter = ihl;
        }
        graph.doLayout(layouter);

        support.sendGraph(graph, response);

    }

}
