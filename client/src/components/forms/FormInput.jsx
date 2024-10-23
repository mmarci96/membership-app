const FormInput = ({ label, ...props }) => {
    return (
        <div className='m-1'>
            <label
                htmlFor={props.id}
                className='block mb-2 text-lg'
            >
                {label}
            </label>
            <input
                required={true}
                {...props}
                className='w-full p-2 mb-4 rounded-lg border-2 border-slate-400 text-md shadow-lg'
            />
        </div>
    );
};

export default FormInput;