function funcD3(d3) {
        'use strict';

        var width = 340;
        var height = 340;
        var radius = Math.min(width, height) / 2;
        var donutWidth = 65;
        var legendRectSize = 10;
        var legendSpacing = 2;

        var color = d3.scale.category20b();

        var svg = d3.select('#chart')
          .append('svg')
          .attr('width', width)
          .attr('height', height)
          .append('g')
          .attr('transform', 'translate(' + (width / 2) + 
            ',' + (height / 2) + ')');

        var arc = d3.svg.arc()
          .innerRadius(radius - donutWidth)
          .outerRadius(radius);

        var pie = d3.layout.pie()
          .value(function(d) { return d.count; })
          .sort(null);

        var tooltip = d3.select('#chart')                               // NEW
          .append('div')                                                // NEW
          .attr('class', 'tooltip');                                    // NEW
                      
        tooltip.append('div')                                           // NEW
          .attr('class', 'label');                                      // NEW
             
        tooltip.append('div')                                           // NEW
          .attr('class', 'count');                                      // NEW

        tooltip.append('div')                                           // NEW
          .attr('class', 'percent');                                    // NEW

        /*d3.csv('weekdays.csv', function(error, dataset) {
          dataset.forEach(function(d) {
            d.count = +d.count;
          });*/
          d3.json('/getPiChartData')
            .header("Content-Type", "application/json")
            .get(function(error, dataset){
              dataset.forEach(function(d) {
              d.count = +d.count;
            });

          var path = svg.selectAll('path')
            .data(pie(dataset))
            .enter()
            .append('path')
            .attr('d', arc)
            .attr('fill', function(d, i) { 
              return color(d.data.label); 
            });

          path.on('mouseover', function(d) {  
            console.log("mouse over .. event ");
            var total = d3.sum(dataset.map(function(d) {                // NEW
              return d.count;                                           // NEW
            }));                                                        // NEW
            var percent = Math.round(1000 * d.data.count / total) / 10; // NEW
            tooltip.select('.label').html(d.data.label);                // NEW
            tooltip.select('.count').html(d.data.count);                // NEW
            tooltip.select('.percent').html(percent + '%');             // NEW
            tooltip.style('display', 'block');                          // NEW
          });                                                           // NEW
          
          path.on('mouseout', function() {                              // NEW
            tooltip.style('display', 'none');                           // NEW
          });                                                           // NEW

          /* OPTIONAL 
          path.on('mousemove', function(d) {                            // NEW
            tooltip.style('top', (d3.event.pageY + 10) + 'px')          // NEW
              .style('left', (d3.event.pageX + 10) + 'px');             // NEW
          });                                                           // NEW
          */
            
          var legend = svg.selectAll('.legend')
            .data(color.domain())
            .enter()
            .append('g')
            .attr('class', 'legend')
            .attr('transform', function(d, i) {
              var height = legendRectSize + legendSpacing;
              var offset =  height * color.domain().length / 2;
              var horz = -2 * legendRectSize;
              var vert = i * height - offset;
              return 'translate(' + horz + ',' + vert + ')';
            });

          legend.append('rect')
            .attr('width', legendRectSize)
            .attr('height', legendRectSize)                                   
            .style('fill', color)
            .style('stroke', color);
            
          legend.append('text')
            .attr('x', legendRectSize + legendSpacing)
            .attr('y', legendRectSize - legendSpacing)
            .text(function(d) { return d; });

        });

      }