const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
            div1: false,
            div2: true,
            cuentaOrigen: "",
            cuentaDestino: "",
            monto: undefined,
            descripcion: "",
            availableAmount: undefined,

        }
    },
    created(){
        axios.get('http://localhost:8080/api/clients/current')
        .then(response =>{
            this.data = response.data
            // console.log(response.data)
        })
        .catch(err => console.log(err))
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
        create(){
            axios.post('/api/clients/current/accounts')
            .then(response => window.location.replace('./accounts.html'))
            .catch(error => {console.error(error)})
        },
        mostrarDiv1() {
            this.div1 = true;
            this.div2 = false;
        },
        mostrarDiv2() {
            this.div2 = true;
            this.div1 = false;
        },
        transferir(){
            Swal.fire({
                title: 'Sure you want to transfer?',
                text: "You won't be able to revert this",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, transfer it'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire(
                        'Transferred',
                        'Succesful transaction',
                        'success'
                    )
                    axios.post('/api/transactions',`amount=${this.monto}&description=${this.descripcion}&sourceNumber=${this.cuentaOrigen}&destinationNumber=${this.cuentaDestino}`)
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
            })
        },
        available(){
            for (let i = 0; i < this.data.accounts.length; i++) {
                if(this.data.accounts[i].number == this.cuentaOrigen){
                    this.availableAmount = this.data.accounts[i].balance
                }
            }
        }
    }
}).mount("#app")
