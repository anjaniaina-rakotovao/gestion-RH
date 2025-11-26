<script>
    export default {
        props: {
            table: {
                type: Array,
                required: true
            },
            columns: {
                type: Array,
                required: true
            }
        },
        computed: {
            tableData() {
                return this.data;
            }
        }
    }
</script>
<table>
    <thead>
        <tr>
            <th v-for="column in columns" :key="column">{{ column }}</th>
        </tr>
    </thead>
    <tbody>
        <tr v-for="(row, rowIndex) in table" :key="rowIndex">
            <td v-for="(cell, cellIndex) in row" :key="cellIndex" v-html="cell"></td>
        </tr>
    </tbody>
    
</table>