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
            Swal.fire({
                title: 'Sure you want to log out?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios
                    .post('/api/logout')
                    .then(response => {window.location.replace('./index.html')})
                    .catch(error => {
                        if (error.response.status === 403) {
                            Swal.fire({
                                icon: 'error',
                                text: error.response.data,
                            })
                        } else {
                            console.log(error)
                        }
                    })
                }
            })},
        enviarRespuestas() {
            axios.post('/api/clients/current/cards',`color=${this.colorTarjeta}&type=${this.tipoTarjeta}`).then(response => console.log('tarjeta creada'))
            .then(response => {
                window.location.replace('./cards.html');
            })
            .catch(error => {
                if (error.response.status === 403) {
                    Swal.fire({
                        icon: 'error',
                        text: error.response.data,
                    })
                } else {
                    console.log(error)
                }
            })
    }
}}).mount("#app")

