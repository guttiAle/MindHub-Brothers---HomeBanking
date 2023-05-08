const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
            // div1: false,
            // div2: true,
            // cuentaOrigen: "",
            // cuentaDestino: "",
            // monto: undefined,
            // descripcion: "",
            // availableAmount: undefined,
            type: "",
            payments: [],
            chosenPayment: undefined,
            monto: undefined,
            client: [],
            cuentaDestino: "",
            chosenLoan: undefined
        }
    },
    created(){
        axios.get('http://localhost:8080/api/loans')
        .then(response =>{
            this.data = response.data
        })
        .catch(err => console.log(err))
        axios.get('http://localhost:8080/api/clients/current')
        .then(response =>{
            this.client = response.data.accounts
            console.log(response.data.accounts)
        })
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
        availablePayments(){
            for (let i = 0; i <this.data.length; i++) {
                if (this.data[i].name === this.type) {
                    this.payments = this.data[i]
                    // this.chosenLoan = this.data
                    console.log(this.data[i])
                }
            }
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
                    axios.post('/api/loans',{ id: this.payments.id, amount: this.monto, payments: this.chosenPayment, destinationAccount: this.cuentaDestino})
                    .then(response => {
                        window.location.replace('./accounts.html');
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
            })
        },
        // available(){
        //     for (let i = 0; i < this.data.accounts.length; i++) {
        //         if(this.data.accounts[i].number == this.cuentaOrigen){
        //             this.availableAmount = this.data.accounts[i].balance
        //         }
        //     }
        // }
    }
}).mount("#app")