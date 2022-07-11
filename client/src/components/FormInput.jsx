import { useState } from "react";

import styled from 'styled-components'

const FormInput = (props) => {
  const [focused, setFocused] = useState(false);
  const { label, errorMessage, onChange, id, ...inputProps } = props;

  const handleFocus = (e) => {
    setFocused(true);
  };

  return (
    <div className="formInput">
      <Label className="mt-2 mb-0">{label}</Label>
      <input
        className="mb-2"
        {...inputProps}
        onChange={onChange}
        onBlur={handleFocus}
        onFocus={() =>
          inputProps.name === "confirmPassword" && setFocused(true)
        }
        focused={focused.toString()}
      />
      <span>{errorMessage}</span>
    </div>
  );
};

export default FormInput;

const Label = styled.label`
    font-size: 18px;
    color: #168082;
    font-weight: bold;
`
