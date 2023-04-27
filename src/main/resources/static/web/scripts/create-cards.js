const {createApp} = Vue
createApp({
    data(){
        return{
            tipoTarjeta: '',
            colorTarjeta: ''
        }
    },
    created(){
    },
    methods: {
        logout() {
            axios
                .post('/api/logout')
                .then(response => {
                window.location.replace('./index.html');
            })
            .catch(error => {
                console.error(error);
            })},
        enviarRespuestas: function() {
            axios.post('/api/clients/current/cards',`color=${this.colorTarjeta}&type=${this.tipoTarjeta}`).then(response => console.log('tarjeta creada'))
            .then(response => {
                window.location.replace('./cards.html');
            })
    }
}}).mount("#app")