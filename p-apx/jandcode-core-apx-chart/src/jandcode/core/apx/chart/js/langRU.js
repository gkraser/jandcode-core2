export default {
    time: {
        month: 'Январь_Февраль_Март_Апрель_Май_Июнь_Июль_Август_Сентябрь_Октябрь_Ноябрь_Декабрь'.split('_'),
        monthAbbr: 'Янв_Фев_Мар_Апр_Май_Июн_Июл_Авг_Сен_Окт_Ноя_Дек'.split('_'),
        dayOfWeek: 'Воскресенье_Понедельник_Вторник_Среда_Четверг_Пятница_Суббота'.split('_'),
        dayOfWeekAbbr: 'Вс_Пн_Вт_Ср_Чт_Пт_Сб'.split('_'),
    },
    legend: {
        selector: {
            all: 'All',
            inverse: 'Inv'
        }
    },
    toolbox: {
        brush: {
            title: {
                rect: 'Box Select',
                polygon: 'Lasso Select',
                lineX: 'Horizontally Select',
                lineY: 'Vertically Select',
                keep: 'Keep Selections',
                clear: 'Clear Selections'
            }
        },
        dataView: {
            title: 'Data View',
            lang: ['Data View', 'Close', 'Refresh']
        },
        dataZoom: {
            title: {
                zoom: 'Zoom',
                back: 'Zoom Reset'
            }
        },
        magicType: {
            title: {
                line: 'Switch to Line Chart',
                bar: 'Switch to Bar Chart',
                stack: 'Stack',
                tiled: 'Tile'
            }
        },
        restore: {
            title: 'Restore'
        },
        saveAsImage: {
            title: 'Save as Image',
            lang: ['Right Click to Save Image']
        }
    },
    series: {
        typeNames: {
            pie: 'Pie chart',
            bar: 'Bar chart',
            line: 'Line chart',
            scatter: 'Scatter plot',
            effectScatter: 'Ripple scatter plot',
            radar: 'Radar chart',
            tree: 'Tree',
            treemap: 'Treemap',
            boxplot: 'Boxplot',
            candlestick: 'Candlestick',
            k: 'K line chart',
            heatmap: 'Heat map',
            map: 'Map',
            parallel: 'Parallel coordinate map',
            lines: 'Line graph',
            graph: 'Relationship graph',
            sankey: 'Sankey diagram',
            funnel: 'Funnel chart',
            gauge: 'Guage',
            pictorialBar: 'Pictorial bar',
            themeRiver: 'Theme River Map',
            sunburst: 'Sunburst'
        }
    },
    aria: {
        general: {
            withTitle: 'This is a chart about "{title}"',
            withoutTitle: 'This is a chart'
        },
        series: {
            single: {
                prefix: '',
                withName: ' with type {seriesType} named {seriesName}.',
                withoutName: ' with type {seriesType}.'
            },
            multiple: {
                prefix: '. It consists of {seriesCount} series count.',
                withName: ' The {seriesId} series is a {seriesType} representing {seriesName}.',
                withoutName: ' The {seriesId} series is a {seriesType}.',
                separator: {
                    middle: '',
                    end: ''
                }
            }
        },
        data: {
            allData: 'The data is as follows: ',
            partialData: 'The first {displayCnt} items are: ',
            withName: 'the data for {name} is {value}',
            withoutName: '{value}',
            separator: {
                middle: ', ',
                end: '. '
            }
        }
    }
}
