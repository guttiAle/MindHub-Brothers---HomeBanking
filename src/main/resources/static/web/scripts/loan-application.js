const {createApp} = Vue
createApp({
    data(){
        return{
            data: [],
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
                }
            }
        },
        transferir(){
            Swal.fire({
                title: 'Sure you want to acquire this loan?',
                text: `${parseInt((this.calcularInteres(this.data, this.chosenPayment , this.type) * this.monto).toFixed(0))}`,
                text:`You'll pay ${this.chosenPayment} payments of $ ${(((this.calcularInteres(this.data, this.chosenPayment , this.type) * this.monto))/this.chosenPayment).toFixed(2)} each. For a total of $ ${parseInt((this.calcularInteres(this.data, this.chosenPayment , this.type) * this.monto).toFixed(0))}`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes'
            })
            .then((result) => {
                if (result.isConfirmed) {
                    Swal.fire(
                        'Success',
                        'You have a new loan',
                        'success'
                    )
                    axios.post('/api/loans',{ id: this.payments.id, amount: this.monto, payments: this.chosenPayment, destinationAccount: this.cuentaDestino})
                    .then(response => {
                        window.location.replace('./accounts.html')
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
        calcularInteres(lista, valor, type) {
            let sum = 1
            if(type == "MORTGAGE"){
                sum += 0.2
            } else if (type == "PERSONAL"){
                sum += 0.1
            } else if(type == "AUTOMOTIVE"){
                sum += 0.15
            }
            for (let i = 0; i < lista.length; i++) {
                if (lista[i].name === type) {
                    for (let j = 0; j < lista[i].payments.length; j++) {
                        if(lista[i].payments[j] == valor){
                            sum += (j + 1) * 0.05
                            return sum
                        }
                    }
                }
            }
            return 0.0;
        }
    }
}).mount("#app")